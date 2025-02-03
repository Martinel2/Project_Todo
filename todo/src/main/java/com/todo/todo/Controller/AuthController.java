package com.todo.todo.Controller;

import com.todo.todo.Util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 실제로는 DB에서 유저를 검증해야 함
        if ("test".equals(username) && "password".equals(password)) {
            String token = jwtUtil.createToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getUser(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        String username = String.valueOf(jwtUtil.verifyToken(token));
        if (username != null) {
            return ResponseEntity.ok(Map.of("username", username));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
    }
}
