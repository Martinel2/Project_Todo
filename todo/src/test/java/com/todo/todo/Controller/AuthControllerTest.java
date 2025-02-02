package com.todo.todo.Controller;

import com.todo.todo.Util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

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
        Mockito.mockStatic(JwtUtil.class);  // Use Mockito to mock static methods if applicable

        when(JwtUtil.createToken("admin")).thenReturn("mock-jwt-token");

        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }


    @Test
    public void testLogin_Success() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-jwt-token"));

        // JwtUtil의 createToken 메서드가 호출되었는지 검증
        verify(jwtUtil, times(1)).createToken("admin");
    }

}
