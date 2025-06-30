package ktlib.infra;

import java.util.Optional;
import java.util.List;
import ktlib.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "manuscriptLists",
    path = "manuscriptLists"
)
public interface ManuscriptListRepository
    extends PagingAndSortingRepository<ManuscriptList, Long> {
        // 수정 List<ManuscriptList> findByManuscriptId(Long manuscriptId);
        Optional<ManuscriptList> findFirstByManuscriptId(Long manuscriptId);

    }

    