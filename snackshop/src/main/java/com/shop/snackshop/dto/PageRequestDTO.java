package com.shop.snackshop.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {//페이지 요청처리(page,size라는 파라미터를 수집하는 역할)

    private int page;
    private int size;
    private String type;//검색기능
    private String keyword;//검색기능

    public PageRequestDTO() {
        //페이지 번호등은 기본값을 가지는 것이 좋기 때문에 1과 10이라는 값을 사용.

        this.page = 1;
        this.size = 10;
        //1페이지에 10개의 게시물
    }

    public Pageable getPageable(Sort sort) {
        //JPA를 이용하는 경우에는 페이지 번호가 0부터 시작한다는 점을 감안해서 1페이지의 경우 0이 될수 있도록 -1을 해준다.

        return PageRequest.of(page -1, size, sort);
    }
}
