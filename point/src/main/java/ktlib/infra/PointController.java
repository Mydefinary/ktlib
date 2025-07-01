package ktlib.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlib.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/points")  // 이 부분은 주석 해제 OK
@Transactional
public class PointController {

    @Autowired
    PointRepository pointRepository;

    @GetMapping("/{id}")
    public Optional<Point> getPoint(@PathVariable Long id) {
        return pointRepository.findById(id);
    }
}

//>>> Clean Arch / Inbound Adaptor

