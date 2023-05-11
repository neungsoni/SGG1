package com.shop.snackshop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.shop.snackshop.entity.QQna;
import com.shop.snackshop.entity.Qna;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class QnaRepositoryTest {

    @Autowired
    private QnaRepository qnaRepository;

    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1,300).forEach(i -> {

            Qna qna = Qna.builder()
                    .title("제목..." + i)
                    .content("내용..." + i)
                    .writer("user" + (i % 10))
                    .build();

            System.out.println(qnaRepository.save(qna));
        });
    }

    @Test
    public void updateTest() {//수정테스트

        Optional<Qna> result = qnaRepository.findById(300L);//존재하는 번호로 테스트

        if (result.isPresent()) {

            Qna qna = result.get();

            qna.changeTitle("제목 수정 테스트");
            qna.changeContent("내용 수정 테스트");

            qnaRepository.save(qna);
        }
    }

    @Test
    public void testQuery1() {//페이지 처리와 동시 검색처리 테스트(동적쿼리)

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QQna qQna = QQna.qna;
        //Q도메인 클래스를 얻어온다(동적처리)

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();
        //where문에 들어가는 조건들을 넣어주는 컨테이너

        BooleanExpression expression = qQna.title.contains(keyword);
        //원하는 조건은 필드 값과 같이 결합해서 생성.

        builder.and(expression);
        //만들어진 조건은 where문에 and나 or같은 키워드와 결합

        Page<Qna> result = qnaRepository.findAll(builder, pageable);

        result.stream().forEach(qna -> {
            System.out.println(qna);
        });
    }

    @Test
    public void testQuery2() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QQna qQna = QQna.qna;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();
        //where문에 들어가는 조건들을 넣어주는 컨테이너

        BooleanExpression exTitle = qQna.title.contains(keyword);
        //원하는 조건은 필드 값과 같이 결합해서 생성.

        BooleanExpression exContent = qQna.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        builder.and(qQna.gno.gt(0L));

        Page<Qna> result = qnaRepository.findAll(builder, pageable);

        result.stream().forEach(qna -> {
            System.out.println(qna);
        });

    }
}
