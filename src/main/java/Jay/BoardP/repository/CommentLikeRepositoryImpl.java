package Jay.BoardP.repository;

import Jay.BoardP.domain.CommentLike;
import Jay.BoardP.domain.QCommentLike;
import Jay.BoardP.repository.customInter.CommentLikeCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentLikeRepositoryImpl implements CommentLikeCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public CommentLikeRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<CommentLike> exist(Long memberId, Long commentId) {

        CommentLike commentLike = jpaQueryFactory.selectFrom(QCommentLike.commentLike)
            .where(QCommentLike.commentLike.member.id.eq(memberId), QCommentLike.commentLike.boardComment.id.eq(commentId))
            .fetchFirst();

        return Optional.ofNullable(commentLike);

    }


}
