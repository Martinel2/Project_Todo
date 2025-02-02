package com.todo.todo.Config;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/json/**").permitAll() // CSS 파일에 대한 접근을 허용
                        .requestMatchers("/user/**", "/myCoupon", "/products/add", "/cart", "/seller**", "/**Coupon","/pay", "/**Wishlist").authenticated()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/") // 접근 거부 페이지 설정
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login-process")
                        .usernameParameter("loginId")	// [C] submit할 아이디
                        .passwordParameter("password")
                        .permitAll()
                        .defaultSuccessUrl("/") // 성공 시 리다이렉트 URL
                        .failureUrl("/login?error") // 실패 시 리다이렉트 URL
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
