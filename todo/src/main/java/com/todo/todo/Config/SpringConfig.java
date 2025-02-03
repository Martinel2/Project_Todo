package com.todo.todo.Config;

import com.todo.todo.Filter.JwtAuthenticationFilter;
import com.todo.todo.Handler.OAuth2SuccessHandler;
import com.todo.todo.Service.OAuth2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringConfig {

    private final OAuth2Service oAuth2Service;
    private final UserDetailsService userDetailsService;
    private final OAuth2SuccessHandler successHandler;

    public SpringConfig(OAuth2Service oAuth2Service, UserDetailsService userDetailsService, OAuth2SuccessHandler successHandler) {
        this.oAuth2Service = oAuth2Service;
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/") // 접근 거부 페이지 설정
                )
                .formLogin(formLogin -> formLogin.disable())

                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                // OAuth2 Login 추가
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service)) // 사용자 정보를 처리할 서비스 지정
                        .successHandler(successHandler) // OAuth2 성공 시 JWT 발급
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

        // JWT 필터 등록
        http.addFilterBefore(new JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
