package com.shop.snackshop.controller;


import com.shop.snackshop.dto.MemberFormDto;
import com.shop.snackshop.entity.Member;
import com.shop.snackshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
@Log4j2 // 4월 4일 추가
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto
                             , BindingResult bindingResult, Model model) {

      /*  Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";*/

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){

    /*    super.doFilter(request, response, filterChain);
        HttpSession session = request.getSession(false);
        if (session != null) {
            IFlash flash = new Flash(session);
            flash.recycle();
        }*/

        return "/member/memberLoginForm";
    }


    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
    // 4월 4일 추가 구문
    @GetMapping(value = "/read")        //사용자 정보 불러오기
    public String userCustom(@AuthenticationPrincipal User user, Model model){
        log.info(user);
        Member member = memberService.loadUser(user.getUsername());
        //memberservice에
        //username email로 검색해서 member에 담는다.


        model.addAttribute("member", member);       // model로 member을 넘긴다.
        return "/member/memberRead";        //memberRead.html로 이동
    }

    @GetMapping(value = "/modify")      //사용자 정보 수정 페이지로 이동
    public String modify(@AuthenticationPrincipal User user, Model model){

        Member member = memberService.loadUser(user.getUsername()); //값을 불러오는건 상동

        MemberFormDto memberFormDto = Member.updateMember(member);
        //member객체에 내용을 그대로 memberFormDto에 담는다.

        //member.updateMember에 member값과 , passwordEncoder 인코더 같이 가지고 감

        model.addAttribute("memberFormDto", memberFormDto);

        return "/member/memberModify";
    }


    @PostMapping(value = "/modify")
    public String memberFormModify(@Valid MemberFormDto memberFormDto
            , BindingResult bindingResult, Model model) {

      /*  Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";*/



        if (bindingResult.hasErrors()) {
            return "member/memberModify";
        }
        try {
            Member member = memberService.loadUser(memberFormDto.getEmail());
            //memberFormDto를 이용 db데이터 가져옴 기존
            boolean checkResult = memberService.checkPassword(member, passwordEncoder);

            if(checkResult){//기존과 입력한 비번의 일치여부
                //비밀번호가 맞지 않습니다. 구현 못함
                log.info(checkResult);
                return "member/memberModify";
            }
            memberService.updateMember(member, memberFormDto);  //주소만 업데이트


        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberModify";
        }
        return "redirect:/members/read";
    }




}

