package Jay.BoardP.etc;

import Jay.BoardP.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPostLikeRepository extends JpaRepository<PostLike, Long>{
}
