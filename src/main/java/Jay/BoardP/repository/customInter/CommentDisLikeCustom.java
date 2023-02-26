package Jay.BoardP.repository.customInter;

import Jay.BoardP.domain.CommentDisLike;
import Jay.BoardP.domain.CommentLike;
import java.util.Optional;

public interface CommentDisLikeCustom {

    public Optional<CommentDisLike> exist(Long memberId, Long commentId);

}
