package Jay.BoardP.repository;

import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.repository.customInter.CommentRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepositoryRepository extends JpaRepository<BoardComment, Long>,
    CommentRepositoryCustom {


    List<BoardComment> findAllByMember_Id(Long memberId);

    @Query("select b from  BoardComment  b where b.id = :id")
    BoardComment findComment(@Param("id") Long id);

}
