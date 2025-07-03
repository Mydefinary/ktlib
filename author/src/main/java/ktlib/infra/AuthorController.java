package ktlib.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlib.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    // 작가 승인
    @PutMapping("/authors/approve/{id}")
    public ResponseEntity<?> authorApprove(@PathVariable Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("author not found"));
            
        author.setStatus("Approved");
        authorRepository.save(author);
        return ResponseEntity.ok().build();
    }

    // 작가 비승인
    @PutMapping("/authors/reject/{id}")
    public ResponseEntity<?> authorReject(@PathVariable Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("author not found"));

        author.setStatus("Rejected");
        authorRepository.save(author);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authors/register")
    public ResponseEntity<?> registerAuthor(@RequestBody Author author) {
    author.setStatus("Pending"); // 최초 상태는 Pending
    Author savedAuthor = authorRepository.save(author);
    return ResponseEntity.ok(savedAuthor);
    }


}
//>>> Clean Arch / Inbound Adaptor
