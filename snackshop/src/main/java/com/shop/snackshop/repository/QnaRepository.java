package com.shop.snackshop.repository;

import com.shop.snackshop.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface QnaRepository extends JpaRepository<Qna, Long>, QuerydslPredicateExecutor<Qna> {

}
