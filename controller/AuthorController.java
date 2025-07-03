package ktlib.controller;

import ktlib.domain.Author;
import ktlib.repository.AuthorRepository;
import ktlib.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
        Author author = new Author();
        author.setAuthorName(request.get("authorName"));
        author.setEmail(request.get("email"));
        author.setAuthorPassword(passwordEncoder.encode(request.get("authorPassword")));
        author.setPhoneNumber(request.get("phoneNumber"));
        author.setPortfolioUrl(request.get("portfolioUrl"));
        author.setAuthorNickname(request.get("authorNickname"));
        author.setStatus("Pending");
        authorRepository.save(author);
        return "Author registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
        Author author = authorRepository.findByEmail(request.get("email")).orElseThrow(() -> new RuntimeException("Author not found"));
        if (!passwordEncoder.matches(request.get("authorPassword"), author.getAuthorPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(author.getEmail(), "author");
        return token;
    }
}
