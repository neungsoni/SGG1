package com.shop.snackshop.repository;





import com.shop.snackshop.dto.ItemSearchDto;
import com.shop.snackshop.dto.MainItemDto;
import com.shop.snackshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    // 상품 조회 조건을 담고 있는 ItemSearchDto 객체와 페이징 정보를 담고 있는 Pageable
    // 객체를 파라미더를 받는 getAdminItemPage 메소드 정의 . 반환  데이터로 Page<Item>
    //객체를 반환
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}

