package com.todo.todo.Config;

import com.todo.todo.Service.OAuth2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringConfig {

    private final OAuth2Service oAuth2Service;

    public SpringConfig(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/") // 접근 거부 페이지 설정
                )
                .formLogin(formLogin -> formLogin.disable()
                )
                // OAuth2 Login 추가
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth/loginInfo", true) // 로그인 성공시 이동할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service)) // 사용자 정보를 처리할 서비스 지정
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().newSession() // 세션 고정 보호
                )
                .httpBasic(Customizer.withDefaults())
                .logout((logout) -> logout
                        .logoutUrl("/logout") // 로그아웃
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));// 세션 무효화


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
