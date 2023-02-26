package Jay.BoardP.etc;

import Jay.BoardP.domain.CommentDisLike;
import Jay.BoardP.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCommentDisLikeRepository extends JpaRepository<CommentDisLike, Long> {

}
