//package com.getcode.controller;
//
//import com.getcode.config.security.SecurityUtil;
//import com.getcode.domain.member.Authority;
//import com.getcode.domain.member.Member;
//import com.getcode.dto.member.SocialSignUpDto;
//import com.getcode.exception.member.NotFoundMemberException;
//import com.getcode.repository.member.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class TestController {
//    private final MemberRepository memberRepository;
//    @Transactional
//    @PostMapping("/login/oauth2/code/api/oauth2/sign-up")
//    public String aa(@RequestBody SocialSignUpDto req) {
//        String currentMemberId = SecurityUtil.getCurrentMemberEmail();
//        log.info(currentMemberId);
//        Member member = memberRepository.findByEmail(currentMemberId).orElseThrow(
//                NotFoundMemberException::new);
//
//        member.updateNickname(req.getNickname());
//        member.updateAuthority(Authority.ROLE_USER);
//        log.info(member.getEmail());
//        log.info(member.getAuthority().toString());
//        return "aa";
//
//    }
//
//}
