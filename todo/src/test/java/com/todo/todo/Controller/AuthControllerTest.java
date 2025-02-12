package com.todo.todo.Controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.todo.todo.Entity.User;
import com.todo.todo.Util.JwtUtil;
import com.todo.todo.Repository.UserRepository;
import com.todo.todo.Dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Configuration
    static class JwtUtilTestConfig {
        @Bean
        public JwtUtil jwtUtil() {
            return new JwtUtil();  // JwtUtil을 빈으로 등록
        }
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLogin_Success() throws Exception {
        String email = "test@example.com";
        String password = "password";

        when(jwtUtil.createAccessToken(email)).thenReturn("mock-jwt-token");

        // When & Then: 로그인 API 호출
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"mock-jwt-token\"}"));

        // JwtUtil의 createToken 메서드가 호출되었는지 검증
        verify(jwtUtil, times(1)).createAccessToken(email);
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        // When & Then: 잘못된 자격 증명으로 로그인 시도
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid@example.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    public void testGetProfile_Success() throws Exception {
        // Given: 유효한 토큰과 사용자
        String token = "valid-token";
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setUsername("testUser");
        user.setProvider("local");

        // ✅ Mock DecodedJWT 객체 생성
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        when(mockDecodedJWT.getSubject()).thenReturn(email);

        // JWT Token을 mock 처리
        when(jwtUtil.verifyToken(token)).thenReturn(mockDecodedJWT);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When & Then: 사용자 프로필 API 호출
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"testUser\",\"email\":\"test@example.com\",\"provider\":\"local\"}"));
    }


    @Test
    public void testGetProfile_Failure_UserNotFound() throws Exception {
        // Given: 유효한 토큰과 존재하지 않는 사용자
        String token = "valid-token";
        String email = "nonexistent@example.com";

        // ✅ Mock DecodedJWT 객체 생성
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        when(mockDecodedJWT.getSubject()).thenReturn(email);

        when(jwtUtil.verifyToken(token)).thenReturn(mockDecodedJWT);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then: 존재하지 않는 사용자로 프로필 요청 시 404 반환
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"User not found\"}"));
    }

}
