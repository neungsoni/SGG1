package com.shop.snackshop.dto;



import com.shop.snackshop.constant.ItemSellStatus;
import com.shop.snackshop.entity.Item;
import lombok.Getter;

import lombok.Setter;
import org.modelmapper.ModelMapper;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;
    @NotBlank(message = "캠페인명은 필수 입력 값입니다")
    private String itemNm;
    @NotNull(message ="후원하실 금액은 필수 입력 값입니다.")
    private Integer price;
    @NotBlank(message ="내용은 필수 입력 값입니다.")
    private String itemDetail;
    @NotNull(message = "인원의 수는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    private List<ItemImgDto> itemImgDtoList =new ArrayList<>();
    // 상품 저장후 수정시 이미지 정보를 저장하는 리스트
    private List<Long> itemImgIds = new ArrayList<>();
    // 이미지 아이디 저장하는 리스트
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this,Item.class);
        //modelMapper를 이용해 엔티티 객체와 DTO객체 간의 데이터 복사. 복사된 객체 반환
    }
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item,ItemFormDto.class);
        //modelMapper를 이용해 엔티티 객체와 DTO객체 간의 데이터 복사. 복사된 객체 반환
    }
    //댓글 관련 추가!

    List<ReplyDTO> replies = new ArrayList<>();

    private int replyCount; // 댓글 조회수
    private String starPoint; // 평점에 관한 구절 추가



}
