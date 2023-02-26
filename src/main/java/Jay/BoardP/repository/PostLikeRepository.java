package Jay.BoardP.repository;

import Jay.BoardP.domain.PostLike;
import Jay.BoardP.repository.customInter.PostLikeCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeCustom {



}
