package com.getcode.config.security;

import com.getcode.config.auth2.CustomOAuthService;
import com.getcode.config.auth2.OAuth2LoginFailureHandler;
import com.getcode.config.auth2.OAuth2LoginSuccessHandler;
import com.getcode.config.jwt.JwtAccessDeniedHandler;
import com.getcode.config.jwt.JwtAuthenticationEntryPoint;
import com.getcode.config.jwt.JwtAuthenticationFilter;
import com.getcode.config.jwt.JwtFilter;
import com.getcode.config.jwt.TokenProvider;
import com.getcode.config.redis.RedisService;
import com.getcode.repository.member.MemberRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuthService customOAuth2UserService;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(getCorsConfiguration()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                // 모든 엔드포인트를 전부 권한검사 X 상태
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
                .headers((e) -> e.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling((e) -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .exceptionHandling((e) -> e.accessDeniedHandler(jwtAccessDeniedHandler))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(tokenProvider, redisService), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(o -> o.userInfoEndpoint(u->u.userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler).failureHandler(oAuth2LoginFailureHandler));
//                .with(new CustomFilterConfigurer(), Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource getCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization");
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // test
//    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity builder) throws Exception {
//            CharacterEncodingFilter filter = new CharacterEncodingFilter();
//            filter.setEncoding("UTF-8");
//            filter.setForceEncoding(true);
//            builder.addFilterBefore(filter, JwtAuthenticationFilter.class);
//            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
//            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
//                    authenticationManager,tokenProvider,
//                    memberRepository, redisService);
//            JwtFilter jwtVerificationFilter = new JwtFilter(tokenProvider, redisService);
//
//            jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
//
//            builder.addFilter(jwtAuthenticationFilter)
//                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
//        }
//    }

}
