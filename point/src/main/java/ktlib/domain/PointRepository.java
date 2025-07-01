package ktlib.domain;

import java.util.Optional;
import java.util.Date;
import java.util.List;
import ktlib.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "points", path = "points")
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByUserId(Long userId);
}

