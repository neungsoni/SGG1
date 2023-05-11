package com.shop.snackshop.repository;



import com.shop.snackshop.dto.CartDetailDto;
import com.shop.snackshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId,Long itemId);
    //카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회
    @Query("select new com.shop.snackshop.dto.CartDetailDto(ci.id,i.itemNm,i.price,ci.count,im.imgUrl)" +
            " from CartItem ci,ItemImg im" +
            " join ci.item i where ci.cart.id = :cartId " +
            "and im.item.id =ci.item.id " +
            "and im.repimgYn ='Y'" +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
