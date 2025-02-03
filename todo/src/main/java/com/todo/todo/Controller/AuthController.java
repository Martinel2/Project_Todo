package com.todo.todo.Controller;

import com.todo.todo.Dto.UserProfile;
import com.todo.todo.Entity.User;
import com.todo.todo.Repository.UserRepository;
import com.todo.todo.Util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        // 실제로는 DB에서 유저를 검증해야 함
        if ("test".equals(email) && "password".equals(password)) {
            String token = jwtUtil.createAccessToken(email);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getProfile(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        log.info("get email");
        String email = jwtUtil.verifyToken(token).getSubject();  // JWT에서 사용자 정보 추출
        log.info("user: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(user.getUsername());
        userProfile.setEmail(user.getEmail());
        userProfile.setProvider(user.getProvider());
        return ResponseEntity.ok(userProfile);
    }

   /* @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getUser(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        String username = String.valueOf(jwtUtil.verifyToken(token));
        if (username != null) {
            return ResponseEntity.ok(Map.of("username", username));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
    }
    */
}
