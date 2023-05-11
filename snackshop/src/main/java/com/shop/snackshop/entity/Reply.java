package com.shop.snackshop.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "item")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer; //회원이 아닌 사람도 댓글 가능
    // 오류 : 비회원시 댓글 안 써짐

    private Long grade; //등급 //이거 댓글관련일것같다 별점으로 추가


    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
}