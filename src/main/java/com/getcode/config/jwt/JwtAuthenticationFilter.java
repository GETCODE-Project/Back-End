package com.getcode.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.dto.member.MemberLoginRequestDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.member.MemberRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        MemberLoginRequestDto loginDto = objectMapper.readValue(request.getInputStream(),
                MemberLoginRequestDto.class);
        UsernamePasswordAuthenticationToken authentication = loginDto.toAuthentication();
        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        TokenDto tokenDto = tokenProvider.generateTokenDto(authResult);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        tokenProvider.setAccessTokenHeader(response, accessToken);
        tokenProvider.setRefreshTokenHeader(response, refreshToken);
        String email = authResult.getName();
        log.info(accessToken);
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        long refreshTokenExpirationMillis = tokenProvider.getRefreshTokenExpirationMillis();
        redisService.setValues(member.getEmail(), refreshToken, Duration.ofMillis(refreshTokenExpirationMillis));
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
