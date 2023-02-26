package Jay.BoardP.repository.customInter;

import Jay.BoardP.domain.CommentLike;
import java.util.Optional;

public interface CommentLikeCustom {

    public Optional<CommentLike> exist(Long memberId, Long commentId);

}
