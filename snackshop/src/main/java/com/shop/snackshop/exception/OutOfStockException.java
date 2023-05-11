package com.shop.snackshop.exception;



public class OutOfStockException extends RuntimeException {// RuntimeException 상속
    // 상품의 주문 수량보다 재고의 수가 적을 시 발생
    public OutOfStockException(String message) {
        super(message);
        // 상품 주문시 상품 재고 감소시키는 로직 ->entity Item에 작성
    }
}
