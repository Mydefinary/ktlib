package ktlib.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final String SECRET_KEY = "yourSecretKeyExample123!";

    @PostMapping
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        // 실제로는 DB에서 사용자 정보 검증
        if ("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            String token = generateToken(loginRequest.getUsername(), "ROLE_USER");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else if ("author".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            String token = generateToken(loginRequest.getUsername(), "ROLE_AUTHOR");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else if ("admin".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            String token = generateToken(loginRequest.getUsername(), "ROLE_ADMIN");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private String generateToken(String username, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 3600000)) // 1시간 유효
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    static class LoginRequest {
        private String username;
        private String password;

        // getters/setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
