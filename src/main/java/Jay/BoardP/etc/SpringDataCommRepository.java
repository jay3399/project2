package Jay.BoardP.etc;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.BoardComment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCommRepository extends JpaRepository<BoardComment, Long> {

    Page<BoardComment> findAllByBoardId(Pageable pageable, Long boardId);

    List<BoardComment> findAllByMember_Id(Long memberId);

}

