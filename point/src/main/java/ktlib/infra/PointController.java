package ktlib.infra;

import java.util.Optional;
import javax.transaction.Transactional;
import ktlib.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor
@RestController
@RequestMapping(value="/points")  // 주석 해제 완료
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

