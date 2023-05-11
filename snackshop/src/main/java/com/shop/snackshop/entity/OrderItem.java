package com.shop.snackshop.entity;




import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) //다대일로 매핑
    @JoinColumn(name = "item_id") // 조인컬럼명 직접 지정
    private Item item;

    @ManyToOne (fetch = FetchType.LAZY)//다대일로 매핑
    @JoinColumn(name = "order_id") // 조인컬럼명 직접 지정
    private Order order;

    private int orderPrice; //주문가격
    private int count; //수량
    //    private LocalDateTime regTime; :삭제  BaseEntity를 상속
//    private LocalDateTime updateTime; :삭제  BaseEntity를 상속
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        // 주문 상품, 주문 수량 세팅
        orderItem.setOrderPrice(item.getPrice());
        //현재 시간 기준으로 상품 가격을 주문 가격으로 세팅
        item.removeStock(count);
        return orderItem;
    }
    public int getTotalPrice() {
        // 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격 계산 메서드
        // 이 메소드를 활용하여서 매출 관리를 만들수 있는가....
        return orderPrice*count;
    }
    public void cancel() {
        this.getItem().addStock(count);
        //주문 취소시 주문 수량만큼 상품의 재고를 더해줌
    }
}



