package com.shop.snackshop.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.shop.snackshop.dto.PageRequestDTO;
import com.shop.snackshop.dto.PageResultDTO;
import com.shop.snackshop.dto.QnaDTO;
import com.shop.snackshop.entity.QQna;
import com.shop.snackshop.entity.Qna;
import com.shop.snackshop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동 주입
public class QnaServiceImpl implements QnaService{

    private final QnaRepository repository;//반드시 final로 선언
    //JPA처리를 위해 QnaRepository를 주입하고 클래스 선언시에 @RequiredArgsConstructor를 이용해서 자동으로 주입.

    @Override
    public Long register(QnaDTO dto) {
        //dtoToEntity()를 활용해서 파라미터로 전달되는 QnaDTO를 변환한다.
        //실제로 데이터 베이스에 처리가 완료 되도록하게 한다.

        log.info("DTO----------------------");
        log.info(dto);

        Qna entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return null;
    }



    @Override
    public void remove(Long gno) {

        repository.deleteById(gno);

    }

    @Override
    public void modify(QnaDTO dto) {

        //수정하는 항목은 제목, 내용
        Optional<Qna> result = repository.findById(dto.getGno());

        if (result.isPresent()) {

            Qna entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            //제목과 내용만을 수정하고 다시 저장하는 방식으로 만든다.

            repository.save(entity);
        }

    }

    @Override
    public QnaDTO read(Long gno) {//QnaService에 정의된 read메소드 기능을 구현한다.
        Optional<Qna> result = repository.findById(gno);
        //findById를 통해서 엔티티 객체를 가져왔다면, entityToDto를 이용해서 엔티티 객체를 DTO를 변환해서 반환.

        return result.isPresent()? entityToDto(result.get()): null;
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {//검색 구현(쿼리dsl처리)

        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QQna qQna = QQna.qna;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qQna.gno.gt(0L);//gno > 0 조건만 생성

        booleanBuilder.and(expression);

        if (type == null || type.trim().length() == 0) {//검색 조건이 없는경우
            return booleanBuilder;
        }

        //검색 조건을 작성(제목, 내용으로만 검색할 수 있게)
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("t")) {
            conditionBuilder.or(qQna.title.contains(keyword));
        }

        if (type.contains("c")) {
            conditionBuilder.or(qQna.content.contains(keyword));
        }
        // 작성자 검색 조건 없어서 넣어 놨어요
        if (type.contains("w")) {
            conditionBuilder.or(qQna.writer.contains(keyword));
        }

        //모든조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

    @Override
    public PageResultDTO<QnaDTO, Qna> getList(PageRequestDTO requestDTO) {
        //JPA의 처리결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고,
        //화면에 페이지 처리와 필요한 값들을 생성한다.

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);//검색 조건 처리

        Page<Qna> result = repository.findAll(booleanBuilder, pageable);//쿼리dsl 사용

        Function<Qna, QnaDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

}
