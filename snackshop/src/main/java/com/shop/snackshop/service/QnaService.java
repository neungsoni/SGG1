package com.shop.snackshop.service;

import com.shop.snackshop.dto.PageRequestDTO;
import com.shop.snackshop.dto.PageResultDTO;
import com.shop.snackshop.dto.QnaDTO;
import com.shop.snackshop.entity.Qna;

public interface QnaService {
    Long register(QnaDTO dto);//QnaDTO를 이용해서 새로운 글을 등록

    QnaDTO read(Long gno);//조회 처리(파라미터는 Long타입의 gno값을 리턴 타입은 QnaDTo로 지정)

    PageResultDTO<QnaDTO, Qna> getList(PageRequestDTO requestDTO);
    //목록처리.
    //PageRequestDTO를 파라미터로,PageResultDTO를 리턴 타입으로 사용하는 getList()메소드를 만든다.

    void remove(Long gno);//삭제처리

    void modify(QnaDTO dto);//수정처리

    default Qna dtoToEntity(QnaDTO dto) {
        //파라미터를 DTO타입으로 받기 때문에 JPA로 처리하기 위해서 엔티티 타입의 객체로 변환하는 작업.

        Qna entity = Qna.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return entity;
    }

    default QnaDTO entityToDto(Qna entity) {
        //엔티티 객체를 DTO객체로 변환하는 entityToDto()를 정의한다.

        QnaDTO dto = QnaDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .build();

        return dto;
    }



}
