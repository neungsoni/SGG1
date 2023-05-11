package com.shop.snackshop.repository;





import com.shop.snackshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>, QuerydslPredicateExecutor<Item>,ItemRepositoryCustom {

    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm,String itemDetail);
    List<Item> findByPriceLessThan(Integer price);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail")String itemDetail);
    @Query(value ="select * from Item i where i.item_detail like %:itemDetail% order by i.price desc",nativeQuery = true)

    List<Item> findByItemDetailByNative(@Param("itemDetail")String itemDetail);

    //여기아래 추가사항 reply
    @Query("select b, r from Item b left  join Reply  r on r.item = b where b.id = :id")
    List<Object[]> getItemWithReply(@Param("id") Long id);

    @Query(value = "select b, count (r) " +
            " from Item b " +
            " left join Reply r on r.item = b " +
            " GROUP BY b",
            countQuery = "select count(b) from Item b")
    Page<Object[]> getItemWithReplyCount(Pageable pageable);        //이걸 리스트형태로 바꿔야하나??
    // 댓글에 대한 거시기
    @Query("select count(r) FROM  Item b " +
            " left  join  Reply r " +
            " on r.item = b" +
            " Where b.id = :id")
    Object getItemById(@Param("id") Long id);
    // grade 평균값 구함 // 별점 거시기
    @Query("select avg(coalesce(r.grade,0)) FROM  Item b " +
            " left  join  Reply r " +
            " on r.item = b" +
            " Where b.id = :id")
    Object getItemByIds(@Param("id") Long id);
}
