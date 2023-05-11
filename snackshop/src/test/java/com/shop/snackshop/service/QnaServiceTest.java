package com.shop.snackshop.service;

import com.shop.snackshop.dto.PageRequestDTO;
import com.shop.snackshop.dto.PageResultDTO;
import com.shop.snackshop.dto.QnaDTO;
import com.shop.snackshop.entity.Qna;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QnaServiceTest {

    @Autowired
    private QnaService service;

    @Test
    public void testRegister() {

        QnaDTO qnaDTO = QnaDTO.builder()
                .title("테스트용 제목")
                .content("테스트용 내용")
                .writer("user0")
                .build();

        System.out.println(service.register(qnaDTO));
    }

    @Test
    public void testList() {//목록처리 테스트

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<QnaDTO, Qna> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV : " + resultDTO.isPrev());//1페이지이므로 이전으로 가는 링크 필요없음
        System.out.println("NEXT : " + resultDTO.isNext());//다음 페이지로 가는 링크 필요
        System.out.println("TOTAL : " + resultDTO.getTotalPage());//전체 페이지 개수

        System.out.println("------------------------------------");
        for (QnaDTO qnaDTO : resultDTO.getDtoList()) {
            System.out.println(qnaDTO);
        }

        System.out.println("====================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));//화면에 출력될 페이지 번호
    }

    @Test
    public void testSearch() {//검색기능 테스트

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")//검색 조건 = t(제목)+c(내용)
                .keyword("한글")//검색 키워드
                .build();

        PageResultDTO<QnaDTO, Qna> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV : " + resultDTO.isPrev());//1페이지이므로 이전으로 가는 링크 필요없음
        System.out.println("NEXT : " + resultDTO.isNext());//다음 페이지로 가는 링크 필요
        System.out.println("TOTAL : " + resultDTO.getTotalPage());//전체 페이지 개수

        System.out.println("------------------------------------");
        for (QnaDTO qnaDTO : resultDTO.getDtoList()) {
            System.out.println(qnaDTO);
        }

        System.out.println("====================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));//화면에 출력될 페이지 번호
    }

}


