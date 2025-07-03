// // UserController.java - 회원가입 대상별 (user, author, admin) 회원가입 코드 추가

// @RestController
// @RequestMapping("/users")
// public class UserController {
//     @Autowired private UserRepository userRepository;
//     @Autowired private JwtUtil jwtUtil;
//     @Autowired private PasswordEncoder passwordEncoder;

//     @PostMapping("/register/user")
//     public String registerUser(@RequestBody User user) {
//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         user.setCarrier("USER");
//         userRepository.save(user);
//         return "User (독자) registered successfully.";
//     }

//     @PostMapping("/register/author")
//     public String registerAuthor(@RequestBody User user) {
//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         user.setCarrier("AUTHOR");
//         userRepository.save(user);
//         return "Author (작가) registered successfully.";
//     }

//     @PostMapping("/register/admin")
//     public String registerAdmin(@RequestBody User user) {
//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         user.setCarrier("ADMIN");
//         userRepository.save(user);
//         return "Admin (관리자) registered successfully.";
//     }

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
//         String email = request.get("email");
//         String password = request.get("password");

//         User user = userRepository.findByEmail(email)
//                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

//         if (!passwordEncoder.matches(password, user.getPassword())) {
//             throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
//         }

//         String token = jwtUtil.generateToken(user.getEmail(), user.getCarrier());

//         Map<String, String> response = new HashMap<>();
//         response.put("token", token);
//         response.put("role", user.getCarrier());
//         response.put("message", "Login successful");

//         return ResponseEntity.ok(response);
//     }
// }

