package com.shop.snackshop.service;


import com.shop.snackshop.dto.MemberFormDto;
import com.shop.snackshop.entity.Member;
import com.shop.snackshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private  final MemberRepository memberRepository;

    public Member saveMember(Member member) {

        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findmember = memberRepository.findByEmail(member.getEmail());
        if (findmember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");

        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        //로그인 로그아웃 기능 구현
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
    public Member loadUser(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        return member;
        //membercontroller에서 사용하는 매서드로 username를 이용하여 db로 부터 사용자 정보를 가져온다.
        // db 연결해서 쓰는게 있는지 확인을 해보자
    }

    //주소 수정 추가
    public  boolean checkPassword(Member member, PasswordEncoder passwordEncoder){
//boolean b = inDBPwd.equals(passwordEncoder.encode(inputPwd); 예시
        Member member1 = memberRepository.findByEmail(member.getEmail()); //email로 기존 db값 가져오기
        //boolean b = passwordEncoder.matches(inDBpwd, inputPwd);
        boolean checkResult = passwordEncoder.matches(member1.getPassword(), member.getPassword());
        //이번에 입력한 패스워드
        return checkResult;

    }
    //주소 수정 추가
    public Member updateMember(Member member, MemberFormDto memberFormDto) {


        member.setAddress(memberFormDto.getAddress());

        return memberRepository.save(member);
    }



}
