package com.getcode.config.auth2;

import com.getcode.config.jwt.TokenDto;
import com.getcode.config.jwt.TokenProvider;
import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Authority;
import com.getcode.domain.member.Member;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            Member member = memberRepository.findBySocialId(
                            ((CustomOAuth2User) authentication.getPrincipal()).getName().toString())
                    .orElseThrow(NotFoundMemberException::new);

            // 사용자 아이디
            String email = String.valueOf(member.getEmail());

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
                // 이미 가입된 회원의 경우 바로 토큰 발급해주면 된다.
                TokenDto tokenDto = tokenProvider.generateTokenDtoOAuth(email, authorities);
                tokenProvider.setAccessTokenHeader(response, tokenDto.getAccessToken());
                tokenProvider.setRefreshTokenHeader(response, tokenDto.getRefreshToken());


                long refreshTokenExpirationMillis = tokenProvider.getRefreshTokenExpirationMillis();
                // 리프레시 토큰을 레디스에 저장
                redisService.setValues(member.getEmail(), tokenDto.getRefreshToken(), Duration.ofMillis(refreshTokenExpirationMillis));
                // 리프레시 토큰의 경우 DB에 저장
//                RefreshToken refreshToken = RefreshToken.builder()
//                        .key(authentication.getName())
//                        .value(tokenDto.getRefreshToken())
//                        .build();
//
//                refreshTokenRepository.save(refreshToken);
                tokenProvider.sendAccessAndRefreshToken(response, tokenDto.getAccessToken(), tokenDto.getRefreshToken());
            }
        } catch (Exception e) {
            throw e;
        }

    }
}