package com.shop.snackshop.repository;





import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.snackshop.constant.ItemSellStatus;
import com.shop.snackshop.dto.ItemSearchDto;
import com.shop.snackshop.dto.MainItemDto;
import com.shop.snackshop.dto.QMainItemDto;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.QItem;
import com.shop.snackshop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    //ItemRepositoryCustom 상속
    private JPAQueryFactory queryFactory;
    // 동적쿼리 생성 클래스
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory =new JPAQueryFactory(em);
        //JPAQueryFactory 생성자로 EntityManager객체를 넣어줌
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        //상품 판매 상태 조건이 전체(null)일 경우는 null을 리턴함,
        // 상품 판매 상태 조건이 null이 아닌 판매중 or 품절 상태라면 해당 조건의 상품만 조회함
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //
    }
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime=dateTime.minusDays(1);
        }else if (StringUtils.equals("1w", searchDateType)) {
            dateTime=dateTime.minusWeeks(1);
        }else if (StringUtils.equals("1m", searchDateType)) {
            dateTime=dateTime.minusMonths(1);
        }else if (StringUtils.equals("6m", searchDateType)) {
            dateTime=dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy,String searchQuery) {
        //searchBy 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회함

        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy",searchBy)) {
            return QItem.item.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression itemNmLike(String searchQuery) { // 검색어가 null이 아니면 상품조회하는 조건 반환
        return StringUtils.isEmpty(searchQuery) ? null:QItem.item.itemNm.like("%"+searchQuery +"%");
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,Pageable pageable) {
        QItem item =QItem.item;
        QItemImg itemImg =QItemImg.itemImg;

        List<MainItemDto> content =queryFactory.select(
                        new QMainItemDto( // QMainItemDto의 생성자에 반환할 값들을 나열
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item,item) // itemImg 와 item 내부조인
                .where(itemImg.repimgYn.eq("Y"))// 상품이미지의 경우 대표 상품 이미지만 불러드림
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;
        return new PageImpl<>(content,pageable,total);
// 페이지 넘기게 설계 해놓음 기존 코드 문제 있습니다
    }

}

