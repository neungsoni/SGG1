package com.shop.snackshop.service;





import com.shop.snackshop.constant.ItemSellStatus;

import com.shop.snackshop.constant.OrderStatus;
import com.shop.snackshop.dto.OrderDto;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.Member;
import com.shop.snackshop.entity.Order;
import com.shop.snackshop.entity.OrderItem;
import com.shop.snackshop.repository.ItemRepository;
import com.shop.snackshop.repository.MemberRepository;
import com.shop.snackshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    public Item saveItem() { // 주문한 상품 저장 메서드

        Item item = new Item();
        item.setItemNm("테스트 상품" );
        item.setPrice(10000 );
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member =new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }
    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        // 주문 상품, 상품 수량을 orderDto 객체에 세팅

        Long orderId = orderService.order(orderDto, member.getEmail());
        // 주문 로직 호출 결과 생성된 주문 번호를 orderId 변수에 저장
        Order order = orderRepository.findById(orderId)
                //주문 번호를 이용해 저장된 주문 정보 조회
                .orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItems =order.getOrderItems();
        int totalPrice =orderDto.getCount()* item.getPrice();
        // 주문한 상품의 총 가격을 구함
        assertEquals(totalPrice,order.getTotalPrice());
        // 총가격과 DB에 저장된 상품의 가격이 비고해 같으면 테스트 종료
    }
    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Item item = saveItem();
        Member member = saveMember();
        // 상품과 회원 데이터 생성 ,상품 재고 100개!!
        OrderDto orderDto =new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId=orderService.order(orderDto,member.getEmail()); // 주문 데이터 생성 재고 10개
        // 생성한 주문 엔티티 조회
        Order order =orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new); // 해당 주문 취소
        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL,order.getOrderStatus());
        assertEquals(100,item.getStockNumber());
        //주문의 상태가 취소면 태스트 성공
        //취소 후 상품의 재고가 처음 갯수와 동일하면 테스트 성공
    }


}