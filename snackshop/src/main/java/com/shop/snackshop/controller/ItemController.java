package com.shop.snackshop.controller;





import com.shop.snackshop.dto.ItemFormDto;
import com.shop.snackshop.dto.ItemSearchDto;
import com.shop.snackshop.entity.Item;
import com.shop.snackshop.service.ItemService;
import com.shop.snackshop.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ReplyService replyService; // 이걸 왜 쓰는거지???
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile")List<MultipartFile>itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
            // 필수 값이 없다면 상품등록 페이지로 전환
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
            //상품 등록시 첫번째 이미지 없다면 에러 메세지와 상품 등록 페이지로 전환
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage","캠페인 등록 중 에러가 발생하였습니다.");
            return "/item/itemForm";
        }
        return "redirect:/";
        // 상품 정상 등록되었으면 메인 페이지로 이동
    }
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId,Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 조회한상품 데이터를 모델에 담아서 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) { // 상품 엔티티가 존재하지 않을 경우 에러 메세지를 담아서 상품등록 페이지로 이동
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto,itemImgFileList); // 상품 수정 로직
        } catch (Exception e) { // 상품 엔티티가 존재하지 않을 경우 에러 메세지를 담아서 상품등록 페이지로 이동
            model.addAttribute("errorMessage","수정 중 에러가 발생하였습니다.");

            return "item/itemForm";
        }
        return "redirect:/";
    }
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    //value에 상품관리 화면 진입시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer>page,Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto,pageable);
        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item>객체를 반환
        model.addAttribute("items",items);
        //조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("itemSearchDto",itemSearchDto);
        //페이지 전환 시 기존 검색 조건을 유지한 채 이동할수 있도록 뷰에 다시 전달
        model.addAttribute("maxPage",5);
        // 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 갯수 (5)
        return "item/itemMng";
    }
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }



}
