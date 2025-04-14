package com.freepath.devpath.common.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize 사용을 위해 설정
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(restAuthenticationEntryPoint) // 인증 실패
                                .accessDeniedHandler(restAccessDeniedHandler)) // 인가 실패
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST,
                                        "/user/signup", "/user/login", "/user/refresh","/user/signup/temp","/user/email/check",
                                        "/user/find-id", "/user/verify-email", "/user/reset-password").permitAll()

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-resources/**",
                                        "/v3/api-docs/**",
                                        "/webjars/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/board/search", "/board/search/content", "/board/category/{category-id:[\\d]+}", "/board/{board-id:[\\d]+}").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/user").authenticated()
                                .requestMatchers(HttpMethod.GET, "/ws-stomp/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/board/*/comments").permitAll()
                                .requestMatchers(HttpMethod.GET, "/board/*/like/count").permitAll()
                                .requestMatchers(HttpMethod.GET, "/comment/*/like/count").permitAll()
                                .requestMatchers("/user/info", "/mypage/**", "/user/change-password","/user/change-email", "/user/dev-mbti").authenticated()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/interview-room/**").hasAuthority("USER")
                                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                                .anyRequest().authenticated()
                )

                // 커스텀 인증 필터(JWT 토큰 사용하여 확인)를 인증 필터 앞에 삽입
                .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public HeaderAuthenticationFilter headerAuthenticationFilter() {
        return new HeaderAuthenticationFilter();
    }
}
