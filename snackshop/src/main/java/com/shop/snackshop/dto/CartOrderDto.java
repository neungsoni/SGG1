package com.shop.snackshop.dto;



import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class CartOrderDto {
    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtoList;
    // 장바구니에서 여려개의 상품 주문
    // CartOrderDto 클래스가 자기 자신을 List로 가지고 있게 만듦
}
