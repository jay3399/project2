package Jay.BoardP.repository.customInter;

import Jay.BoardP.domain.BoardComment;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {


//    public Long save(BoardComment comment);

//    public BoardComment findById(Long id);

    public List<BoardComment> findByBoardId(Long boardId);

    public List<BoardComment> findByBoardIdV2(Long boardId);

    public Long totalComments(Long boardId);

    public Optional<BoardComment> findByIdWithParent(Long commentId);



}
