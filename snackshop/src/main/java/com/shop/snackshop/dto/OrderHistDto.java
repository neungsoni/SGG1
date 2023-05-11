package com.shop.snackshop.dto;





import com.shop.snackshop.constant.OrderStatus;
import com.shop.snackshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    private Long orderId; // 주문 아이디
    private String orderDate; // 주문 날짜
    private OrderStatus orderStatus; // 주문 상태
    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList =new ArrayList<>();

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate= order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

    }
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        //OrderItemDto 객체를 주문 상품리스트에 추가 하는 메서드
        orderItemDtoList.add(orderItemDto);
    }

}
