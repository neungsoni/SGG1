package com.shop.snackshop.dto;





import com.shop.snackshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String itemNm; // 상품명
    private int count;// 주문 수량
    private int orderPrice; // 주문 금액
    private String imgUrl; // 상품의 이미지 경로
    public OrderItemDto(OrderItem orderItem,String imgUrl) {
        // OrderItemDto 클래스 생성자
        //OrderItem 객체와 이미지 경로를 파라미터를 받아서
        // 멤버 변수 값을 세팅
        this.itemNm = orderItem.getItem().getItemNm();
        this.count= orderItem.getCount();
        this.orderPrice=orderItem.getOrderPrice();
        this.imgUrl=imgUrl;
    }
}
