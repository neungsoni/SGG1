package com.shop.snackshop.service;





import com.shop.snackshop.entity.ItemImg;
import com.shop.snackshop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")//application.properties에 있는 itemImgLocation 프로퍼티 값을 읽어 옴
    private String itemImgLocation;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName ="";
        String imgUrl ="";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName=fileService.uploadFile(itemImgLocation,oriImgName,itemImgFile.getBytes());
            //uploadFile 파일 메서드 호출 로컬에 저장된 파일의 이름은 imgName 변수에 저장
            imgUrl = "/images/item/" + imgName;
        }
        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);
        // 입력받은 상품 이미지 정보를 저장.
    }
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile)throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName =fileService.uploadFile(itemImgLocation,oriImgName,itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName,imgName,imgUrl);
            // 변경된 상품 이미지를 세팅 save로 호출하지 않음 savedItemImg 엔티티는 영속 상태이므로
            //데이터를 변경하는 것 만으로 변경 감지 기능이 동작하여 트랜젝션이 끝날떄 update 쿼리가 실행
            // 중요!!! 엔티티가 영속 상태 여야 함
        }
    }
}
