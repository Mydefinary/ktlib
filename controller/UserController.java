package ktlib.controller;

import ktlib.domain.User;
import ktlib.repository.UserRepository;
import ktlib.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
        User user = new User();
        user.setName(request.get("name"));
        user.setEmail(request.get("email"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setRegisteredAt(new Date());
        user.setCarrier(request.get("carrier"));
        user.setPoint(Long.parseLong(request.getOrDefault("point", "0")));

        // ✅ 일반 회원 가입 시 role 지정
        user.setRole("USER");

        userRepository.save(user);
        return "User registered";
    }

    @PostMapping("/register-admin")
    public String registerAdmin(@RequestBody Map<String, String> request) {
        User user = new User();
        user.setName(request.get("name"));
        user.setEmail(request.get("email"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setRegisteredAt(new Date());
        user.setCarrier(request.get("carrier"));
        user.setPoint(Long.parseLong(request.getOrDefault("point", "0")));

        // ✅ 관리자 회원 가입 시 role 지정
        user.setRole("ADMIN");

        userRepository.save(user);
        return "Admin user registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
        User user = userRepository.findByEmail(request.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.get("password"), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // ✅ JWT 발급 시 role 포함
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return token;
    }
}