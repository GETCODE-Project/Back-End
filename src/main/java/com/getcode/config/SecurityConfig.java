package com.getcode.config;

import com.getcode.config.auth.CustomOAuth2UserService;
import com.getcode.config.auth.MyAuthenticationSuccessHandler;
import com.getcode.config.jwt.JwtAccessDeniedHandler;
import com.getcode.config.jwt.JwtAuthenticationEntryPoint;
import com.getcode.config.jwt.JwtFilter;
import com.getcode.config.jwt.JwtSecurityConfig;
import com.getcode.config.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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

    private final CustomOAuth2UserService customOAuth2UserService;
    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtFilter jwtFilter;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("*"));
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
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//                .apply(new JwtSecurityConfig(tokenProvider));
//        http.oauth2Login(o ->o.userInfoEndpoint(u->u.userService(customOAuth2UserService)).defaultSuccessUrl("/loginInfo", true));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
