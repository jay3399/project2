package Jay.BoardP.repository.customInter;

import Jay.BoardP.domain.PostLike;
import java.util.Optional;

public interface PostLikeCustom {


    public Optional<PostLike> exist(Long memberId, Long boardId);

    public Long findPostLikeNum(Long boardId);



}
