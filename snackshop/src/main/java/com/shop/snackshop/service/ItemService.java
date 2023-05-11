package com.shop.snackshop.service;





import com.shop.snackshop.dto.*;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.entity.ItemImg;
import com.shop.snackshop.repository.ItemImgRepository;
import com.shop.snackshop.repository.ItemRepository;
import com.shop.snackshop.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    //댓글 추가 부분
    private final ReplyService replyService;
    private final ReplyRepository replyRepository;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile>
            itemImgFileList) throws Exception {
        //상품 등록
        Item item = itemFormDto.createItem();
        // 상품등록 폼으로부터 입력 받은 데이터를 이용하여 Item 객체를 생성
        itemRepository.save(item);
        //상품데이터 저장

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg =new ItemImg();
            itemImg.setItem(item);
            if (i==0) {
                itemImg.setRepimgYn("Y");
            } //첫번쨰 이미지일 경우 대표 상품으로 이미지 여부값을 Y로 세팅
            else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
            // 상품 이미지 정보를 저장
        }
        return item.getId();
    }
    //댓글 관련 수정있음!
    @Transactional(readOnly = true)
    //트랜잭션을 읽기전용으로 설정, JPA가 변경감지를 수행 안해 성능 항상
    public ItemFormDto getItemDtl(Long itemId) {
        //트랜잭션을 읽기전용으로 설정, JPA가 변경감지를 수행 안해 성능 항상
        List<ItemImg> itemImgList=
                itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //해당 상품의 이미지를 조회함, 등록순으로 가지고 오기 위해 상품 이미지 아이디를 오름차순으로 가지고 옴
        List<ItemImgDto> itemImgDtoList =new ArrayList<>();
        //이미지Dto객체로 리스트 , 객체생성, 이미지 이름등 url등 이미지에 관련된 객체

        //댓글관련  댓글리스트를 담은 값 !
        List<ReplyDTO> replies = replyService.getList(itemId);

        for (ItemImg itemImg : itemImgList) {
            //조회한 ItemImg엔티티를 ItemImgDto객체로 만들어서 리스트에 추가
            ItemImgDto itemImgDto =ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
            //리스트 화
        }
        //댓글 관련 추가! getItemById를 사용
        int replyCount = Integer.parseInt(String.valueOf(itemRepository.getItemById(itemId)));
        //평점 관련 추가 getItemByIds를 사용
        String starpoint = String.valueOf(itemRepository.getItemByIds(itemId));
        Item item = itemRepository.findById(itemId)//상품의 아이디를 통해 상품 엔티티를 조회함
                .orElseThrow(EntityNotFoundException::new);//존재하지 않을 때 EntityNotFoundException발생
        ItemFormDto itemFormDto =ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        //댓글 리스트 추가!
        itemFormDto.setReplies(replies);
        itemFormDto.setReplyCount(replyCount);
        //평점 추가
        itemFormDto.setStarPoint(starpoint);
        return itemFormDto;
    }
    public Long updateItem(ItemFormDto itemFormDto,List<MultipartFile> itemImgFileList) throws Exception {
        //상품 수정
        Item item =itemRepository.findById(itemFormDto.getId())// 아이디 이용해서 상품 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);// 상품 엔티티 업데이트
        List<Long> itemImgIds =itemFormDto.getItemImgIds();//상품 이미지 아이디 리스트 조회
        //이미지 등록

        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
            //updateItemImg 메소드에 상품이미지아이디,상품이미지파일정보 파라미터 전달
        }
        return item.getId();
    }
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }
    //메인페이지에 상품 데이터를 보여주기
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto,pageable);
    }


}
