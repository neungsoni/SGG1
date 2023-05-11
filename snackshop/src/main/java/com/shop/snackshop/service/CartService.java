package com.shop.snackshop.service;



import com.shop.snackshop.dto.CartDetailDto;
import com.shop.snackshop.dto.CartItemDto;
import com.shop.snackshop.dto.CartOrderDto;
import com.shop.snackshop.dto.OrderDto;
import com.shop.snackshop.entity.Cart;
import com.shop.snackshop.entity.CartItem;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.Member;
import com.shop.snackshop.repository.CartItemRepository;
import com.shop.snackshop.repository.CartRepository;
import com.shop.snackshop.repository.ItemRepository;
import com.shop.snackshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto,String email) {
        // 장바구니에 담을 상품 엔티티 조회
        Item item=itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        //현재 로그인한 회원 엔티티 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        //현재 로그인한 회원의 장바구니 엔티티를 조회
        if (cart == null) {
            //4.상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        CartItem savedCartItem =
                cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        //현재 상품이 장바구니에 있는지 조회
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
            // 이미 있는 상품일시 기존 수량에서 현재 장바구니에 담을 수량을 더함
        } else {
            CartItem cartItem =
                    CartItem.createCartItem(cart,item,cartItemDto.getCount());
            //장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수령을 이용 카트아이템 엔티티 생성
            cartItemRepository.save(cartItem);
            //장바구니에 들어갈 상품 저장
            return cartItem.getId();
        }

    }
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList =new ArrayList<>();
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return cartDetailDtoList;
        }
        cartDetailDtoList=cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        //현재 로그인한 회원을 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        // 장바구니 상품을 저장한 회원을 조회
        if (!StringUtils.equals(curMember.getEmail(),
                savedMember.getEmail())) {
            return false;
        }
        return true;
        //로그인 한 회원과 장바구니 상품을 저장한 회원이 다를경우 false 같으면 true를 반환
    }
    public void updateCartItemCount(Long cartItemId, int count) {
        // 장바구니 상품의 수량을 업데이트 하는 메소드
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }
    //장바구니 상품번호 삭제하는 로직 (파라미터)
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }
    //orderDto 리스트 생성 및 주문 로직 호출
    // 주문한 상품은 장바구니에서 제거하는 로직 구현
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList,String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            //1.장바구니 페이지에서 전달받은 주문 상품 번호를 이용하여
            //주문 로직으로 전달할 orderDto 객체를 만듬
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }
        Long orderId = orderService.orders(orderDtoList,email);
        //장바구니에 담은 상품을 주문하도록 주문 로직 호출
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // 주문한 상품들을 장바구니에서 제거
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }

}
