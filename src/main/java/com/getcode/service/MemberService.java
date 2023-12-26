package com.getcode.service;

import com.getcode.domain.member.Member;
import com.getcode.dto.SignUpDto;
import com.getcode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signup(SignUpDto signUpDto) {
        Member member = signUpDto.toEntity();
        member.passwordEncoding(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

}
