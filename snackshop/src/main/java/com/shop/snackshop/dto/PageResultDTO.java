package com.shop.snackshop.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {//페이지 결과 처리
    //page<Entity>의 엔티티 객체들을 DTO객체로 변환해서 자료구조로 담아주어야 한다.
    //화면 출력에 필요한 페이지 정보들을 구성해 주어야 한다.

    private List<DTO> dtoList;
    //DTO리스트

    private int totalPage;
    //총 페이지 번호

    private int page;
    //현재 페이지 번호

    private int size;
    //목록 사이즈

    private int start, end;
    //시작 페이지번호,끝 페이지번호

    private boolean prev, next;
    //이전, 다음

    private List<Integer> pageList;
    //페이지 번호 목록



    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        //PageResultDTO클래스는 다양한 곳에서 사용할 수 있도록 제네릭 타입을 이용해서 DTO,EN이라는 타입을 지정.
        //(DTO와 Entity타입을 의미한다.)

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;//0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;//Math.ceil은 소수점을 올림으로 처리한다.

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd: totalPage;
        next = totalPage > tempEnd;//tempEnd = 끝번호는 계속 늘어날수 있으므로 사용한다.

        //이전(prev)의 경우는 시작 번호(start)가 1보다 큰경우라면 존재하게 된다.
        //다음(next)으로 가는 링크는 위의 realEnd가 끝번호(end)보다 큰경우에만 존재하게 된다.
        //결론:페이징처리(1페이지당 10개의 게시물을 보여지게)

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }


}
