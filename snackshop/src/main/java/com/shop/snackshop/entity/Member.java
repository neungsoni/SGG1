package com.shop.snackshop.entity;

import com.shop.snackshop.constant.Role;
import com.shop.snackshop.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private  String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
    // 230404 13:03 추가 멤버 구문
    public static MemberFormDto updateMember(Member member) {


        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setName(member.getName());
        memberFormDto.setEmail(member.getEmail());


        memberFormDto.setPassword(member.getPassword());
        memberFormDto.setAddress(member.getAddress());

        member.setRole(member.getRole());
        return memberFormDto;
    }

}
