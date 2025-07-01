package ktlib.infra;

import java.util.Optional;
<<<<<<< HEAD
=======
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
import javax.transaction.Transactional;
import ktlib.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

//<<< Clean Arch / Inbound Adaptor
@RestController
@RequestMapping(value="/points")  // 주석 해제 완료
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/points")
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
@Transactional
public class PointController {

    @Autowired
    PointRepository pointRepository;
<<<<<<< HEAD

    @GetMapping("/{id}")
    public Optional<Point> getPoint(@PathVariable Long id) {
        return pointRepository.findById(id);
    }
}
//>>> Clean Arch / Inbound Adaptor

=======
}
//>>> Clean Arch / Inbound Adaptor
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
