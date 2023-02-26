package Jay.BoardP.etc;

import Jay.BoardP.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface SpringDataCommentLikeRepository extends JpaRepository<CommentLike , Long> {

}
