package Jay.BoardP.etc;

import Jay.BoardP.domain.Penalty;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPenaltyRepository extends JpaRepository<Penalty, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Penalty> findAllByBoardId(Long boardId);

}
