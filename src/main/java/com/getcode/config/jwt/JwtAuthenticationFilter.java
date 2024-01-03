package com.getcode.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getcode.config.redis.RedisService;
import com.getcode.domain.member.Member;
import com.getcode.dto.member.MemberLoginRequestDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

        Long memberId = Long.parseLong(authResult.getName());
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Long refreshTokenExpiresIn = tokenDto.getRefreshTokenExpiresIn();
        redisService.setValues(member.getEmail(), refreshToken, Duration.ofMillis(refreshTokenExpiresIn));

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
