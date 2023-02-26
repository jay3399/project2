package Jay.BoardP.repository;

import Jay.BoardP.domain.CommentLike;
import Jay.BoardP.repository.customInter.CommentLikeCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeCustom {


}
