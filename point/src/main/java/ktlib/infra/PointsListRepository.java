package ktlib.infra;

import java.util.List;
import ktlib.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource(
    collectionResourceRel = "pointsLists",
    path = "pointsLists"
)
public interface PointsListRepository
    extends PagingAndSortingRepository<PointsList, Long> {
            Optional<PointsList> findFirstByPointId(Long pointId);  // 추가 code

    }
