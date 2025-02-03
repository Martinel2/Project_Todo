package com.todo.todo.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "mySecretKey";  // 비밀 키

    // JWT 생성
    public static String createToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); // 비밀 키로 서명
        return JWT.create()
                .withSubject(username)           // 사용자 이름
                .withIssuedAt(new Date())        // 발행 시간
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 만료 시간 (1시간)
                .sign(algorithm);                // 서명
    }

    // JWT 검증 및 파싱
    public static DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);  // 비밀 키로 서명 검증
        return JWT.require(algorithm)    // 서명 검증용 알고리즘 설정
                .build()
                .verify(token);            // 토큰 검증
    }
}
