package com.shop.snackshop.dto;



import lombok.Getter;
import lombok.Setter;
//EL1008E: Property or field 'name' cannot be found on object of type 'Bulletin.Board.domain.posts.User' - maybe not public or not valid?
//getter setter 에러 DTO 확인
//
@Getter
@Setter
public class CartDetailDto {
    private Long cartItemId; // 장바구니 상품 아이디
    private String itemNm; // 상품명
    private int price; // 상품 금액
    private int count; // 수량
    private String imgUrl; // 상품 이미지 경로
    public CartDetailDto(Long cartItemId, String itemNm, int price, int
            count, String imgUrl) {
        //장바구니 페이지에 전달할 데이터를 생성자의 파라미터로 만들어줌
        this.cartItemId=cartItemId;
        this.itemNm=itemNm;
        this.price=price;
        this.count=count;
        this.imgUrl=imgUrl;
    }
}
