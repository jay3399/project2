package Jay.BoardP.repository;

import Jay.BoardP.domain.Penalty;
import Jay.BoardP.repository.customInter.PenaltyCustom;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty , Long> , PenaltyCustom {

    @EntityGraph(attributePaths = {"member"})
    List<Penalty> findAllByBoardId(Long boardId);


}
