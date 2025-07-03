package ktlib.infra;

import java.util.Date;
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
import ktlib.infra.ManuscriptUpdateDto;



//<<< Clean Arch / Inbound Adaptor

@RestController
//@RequestMapping("/manuscripts")
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @PutMapping("/manuscripts/publish/{id}") 
    public ResponseEntity<?> requestPublish(@PathVariable Long id) {
        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Manuscript not found"));

        manuscript.setStatus("RequestedPublish");
        manuscript.setPublishRequested(true);
        manuscriptRepository.save(manuscript);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/manuscripts/update/{id}")
    public ResponseEntity<?> updateContent(@PathVariable Long id, @RequestBody ManuscriptUpdateDto dto) {
        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Manuscript not found"));

        manuscript.setTitle(dto.getTitle());
        manuscript.setContent(dto.getContent());
        manuscript.setContentUpdated(true);
        manuscriptRepository.save(manuscript);

        return ResponseEntity.ok().build();
    }
}
//>>> Clean Arch / Inbound Adaptor
