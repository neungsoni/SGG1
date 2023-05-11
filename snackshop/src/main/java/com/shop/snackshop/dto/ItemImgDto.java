package com.shop.snackshop.dto;





import com.shop.snackshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter@Setter
public class ItemImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper =new ModelMapper();
    // 멤버의 변수로 ModelMapper 객체 추가
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
        //itemImg 엔티티 객체를 파라미터로 받아서 멤버변수의 이름이 같을때
        //ItemImgDto로 값을 복사 해서 반환 static메서고 선언으로
        // 객체 생성없이 호출 가능
    }
}
