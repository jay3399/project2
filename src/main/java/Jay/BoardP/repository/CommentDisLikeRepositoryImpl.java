package Jay.BoardP.repository;

import static Jay.BoardP.domain.QCommentDisLike.commentDisLike;

import Jay.BoardP.domain.CommentDisLike;
import Jay.BoardP.repository.customInter.CommentDisLikeCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentDisLikeRepositoryImpl implements CommentDisLikeCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public CommentDisLikeRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<CommentDisLike> exist(Long memberId, Long commentId) {
        CommentDisLike commentLike = jpaQueryFactory.selectFrom(commentDisLike)
            .where(commentDisLike.member.id.eq(memberId), commentDisLike.boardComment.id.eq(commentId))
            .fetchFirst();

        return Optional.ofNullable(commentLike);
    }
}
