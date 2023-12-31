package com.getcode.config.security;

import com.getcode.config.auth2.CustomOAuthService;
import com.getcode.config.auth2.OAuth2LoginFailureHandler;
import com.getcode.config.auth2.OAuth2LoginSuccessHandler;
import com.getcode.config.jwt.JwtAccessDeniedHandler;
import com.getcode.config.jwt.JwtAuthenticationEntryPoint;
import com.getcode.config.jwt.JwtFilter;
import com.getcode.config.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuthService customOAuth2UserService;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(Arrays.asList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                return config;
            }}))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
                .headers((e) -> e.frameOptions((a) -> a.sameOrigin()))
                .exceptionHandling((e) -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .exceptionHandling((e) -> e.accessDeniedHandler(jwtAccessDeniedHandler))
                .formLogin(f -> f.disable())
                .httpBasic(h -> h.disable())
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(o -> o.userInfoEndpoint(u->u.userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler).failureHandler(oAuth2LoginFailureHandler));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
