package ktlib.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "points", path = "points")
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByUserId(Long userId);
}
