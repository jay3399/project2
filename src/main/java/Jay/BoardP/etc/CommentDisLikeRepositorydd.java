package Jay.BoardP.etc;

import static Jay.BoardP.domain.QCommentDisLike.commentDisLike;

import Jay.BoardP.domain.CommentDisLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDisLikeRepositorydd {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;


    public CommentDisLikeRepositorydd(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<CommentDisLike> exist(Long memberId, Long commentId) {

        CommentDisLike commentLike = jpaQueryFactory.selectFrom(commentDisLike)
            .where(commentDisLike.member.id.eq(memberId), commentDisLike.boardComment.id.eq(commentId))
            .fetchFirst();

        return Optional.ofNullable(commentLike);
    }

}
