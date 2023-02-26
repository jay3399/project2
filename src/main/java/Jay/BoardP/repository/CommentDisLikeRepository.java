package Jay.BoardP.repository;

import Jay.BoardP.domain.CommentDisLike;
import Jay.BoardP.repository.customInter.CommentDisLikeCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDisLikeRepository extends JpaRepository<CommentDisLike, Long> ,
    CommentDisLikeCustom {

}
