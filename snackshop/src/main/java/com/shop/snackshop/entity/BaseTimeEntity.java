package com.shop.snackshop.entity;





import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter @Setter
@EntityListeners(value = {AuditingEntityListener.class})// auditing 적용 어노테이션
@MappedSuperclass// 공통 매핑 정보 필요시 사용하는 어노테이션 (부모에게 상속받은 자식 매핑 정보만 제공)
public class BaseTimeEntity {
    @CreatedDate // 엔티티 생성,저장시 시간 자동저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티 값 변경시 시간 자동저장
    private LocalDateTime updateTime;
}
