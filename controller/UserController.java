package ktlib.controller;

import ktlib.domain.User;
import ktlib.domain.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        user.setRegisteredAt(new java.util.Date());
        user.setPoint(0L); // 초기 포인트 0
        return userRepository.save(user);
    }
}
