package com.getcode.config.auth2;

import com.getcode.config.jwt.TokenDto;
import com.getcode.config.jwt.TokenProvider;
import com.getcode.domain.member.Authority;
import com.getcode.domain.member.Member;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            log.info(authentication.toString());
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            Member member = memberRepository.findBySocialId(
                            ((CustomOAuth2User) authentication.getPrincipal()).getName().toString())
                    .orElseThrow(NotFoundMemberException::new);

            // 사용자 아이디
            String id = String.valueOf(member.getId());

            // 사용자 권한
            String authorities = oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if (oAuth2User.getAuthority() == Authority.ROLE_GUEST) {
                // 변경
                String accessToken = tokenProvider.createAccessToken(oAuth2User.getEmail());
                response.addHeader("Authorization", "Bearer " + accessToken);
                response.sendRedirect("api/oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
                log.info(accessToken);
                tokenProvider.sendAccessToken(response, accessToken);
            } else {
                TokenDto tokenDto = tokenProvider.generateTokenDtoOAuth(id, authorities);
                log.info(tokenDto.getAccessToken());
            }
        } catch (Exception e) {
            throw e;
        }

    }
}