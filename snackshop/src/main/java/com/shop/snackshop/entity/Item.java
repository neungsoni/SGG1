package com.shop.snackshop.entity;





import com.shop.snackshop.constant.ItemSellStatus;
import com.shop.snackshop.dto.ItemFormDto;
import com.shop.snackshop.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="item")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseEntity {
    @Id
    @Column(name ="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드
    @Column(nullable = false,length = 50)
    private String itemNm; // 상품명
    @Column(name ="price",nullable = false)
    private int price; //가격
    @Column(nullable = false)
    private int stockNumber; // 재고수량
    @Lob
    //BLOB, CLOB 타입 매핑
    // CLOB : 사이즈가 큰 테이터를 외부 파일로 저장하기 위한 데이터 타입 (문자형 대용량 파일 저장)
    // BLOB : 바이너리 데이터를 DB외부에 저장하기 위한 타입 (이미지, 사운드, 비디오 : 멀티미디어)
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태
    //    private LocalDateTime regTime; // 등록 시간  BaseEntity상속 삭제
//    private LocalDateTime updateTime; //수정시간 BaseEntity상속 삭제
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm=itemFormDto.getItemNm();
        this.price= itemFormDto.getPrice();
        this.stockNumber=itemFormDto.getStockNumber();
        this.itemDetail=itemFormDto.getItemDetail();
        this.itemSellStatus=itemFormDto.getItemSellStatus();
    }
    public void removeStock(int stockNumber) {
        int restStock =this.stockNumber -stockNumber;
        // 주문 후 남은 재고 수량 구함
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다." +
                    "현재 재고 수량:"+this.stockNumber+ ")");
            // 재고가 주문 수량보다 작으면 재고 부족 예외 발생
        }
        this.stockNumber=restStock;
        // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당
    }
    public void addStock(int stockNumber) {
        // 상품의 재고를 증가시키는 메소드
        this.stockNumber += stockNumber;
    }
}

