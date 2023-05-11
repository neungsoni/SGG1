package com.shop.snackshop.entity;




import com.shop.snackshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @Column(name="order_id")
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)//다대일로 매핑
    @JoinColumn(name = "member_id") // 조인컬럼명 직접 지정
    private Member member;

    private LocalDateTime orderDate; // 주문일
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)

    //부모엔티티의 영속 상태를 자식 엔티티에게 전이시키는 옵션 CascadeType.ALL
    // 고아 객체 제거 (orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    //    private LocalDateTime regTime;   extends BaseEntity 삭제
//    private LocalDateTime updateTime; extends BaseEntity 삭제
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public static Order createOrder(Member member,List<OrderItem>orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 "ORDER"로 세팅
        order.setOrderDate(LocalDateTime.now());// 현재 시간을 주문 시간으로 세팅
        return order;
    }
    //총 주문을 구하는 메서드
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    public void cancelOrder() {
        this.orderStatus=OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }



}
