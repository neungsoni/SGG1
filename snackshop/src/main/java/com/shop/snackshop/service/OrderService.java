package com.shop.snackshop.service;





import com.shop.snackshop.dto.OrderDto;

import com.shop.snackshop.dto.OrderHistDto;
import com.shop.snackshop.dto.OrderItemDto;
import com.shop.snackshop.entity.*;
import com.shop.snackshop.repository.ItemImgRepository;
import com.shop.snackshop.repository.ItemRepository;
import com.shop.snackshop.repository.MemberRepository;
import com.shop.snackshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                // 주문할 상품을 조회
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        //현재 로그인 한 회원의 이메일 정보로 회원 정보 수정
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        //주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티 생성
        orderItemList.add(orderItem);

//        for(int i =0; i< orderItemList.size(); i++){
//
//        }
//
//        int price = orderItem.getOrderPrice();


        Order order = Order.createOrder(member, orderItemList);
        //
        // 회정, 주문할 상품 리스트 정보를 이용 주문 엔티티 생성
        orderRepository.save(order);
        // 생성한 주문 엔티티 저장

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) { //주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO 생성  에러
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");// 주문한 상품의 대표 이미지 조회
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
        //페이지 구현 객체를 생성하여 반환
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        //주문 데이터 생성자와 로그인 사용자 같은지 검사
        // 같은때는 true 같지 않을 경우 false를 반환
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }
    public void cancelOrder(Long orderId) {
        Order order =orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
        //주문 취소 상태로 변경시 변경 감지기능
        // 트랜잭션 끝날 떄 update 쿼리가 실행
    }
    public Long orders(List<OrderDto> orderDtoList,String email) {
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            //주문할 상품 리스트를 만들어줌
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount());
            orderItemList.add(orderItem);
        }
        Order order =Order.createOrder(member,orderItemList);
        // 현재 로그인한 회원과 주문 상품 목록을 이용하여 주문 엔티티 만듬
        orderRepository.save(order);
        //주문 데이터를 저장
        return order.getId();
    }
}