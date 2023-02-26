package Jay.BoardP.etc;



import static Jay.BoardP.domain.QCommentLike.commentLike;

import Jay.BoardP.domain.CommentLike;
import Jay.BoardP.domain.QCommentLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class CommentLikeRepositorydd {


    private final EntityManager em ;
    private final JPAQueryFactory jpaQueryFactory;

    public CommentLikeRepositorydd(EntityManager entityManager) {
        this.em = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<CommentLike> exist(Long memberId, Long commentId) {

        CommentLike commentLike = jpaQueryFactory.selectFrom(QCommentLike.commentLike)
            .where(QCommentLike.commentLike.member.id.eq(memberId), QCommentLike.commentLike.boardComment.id.eq(commentId))
            .fetchFirst();


        return Optional.ofNullable(commentLike);
    }


    // 이거

    // vs 해당 보드엔티티로 직접 접근해서 가져오기 .. ? -> 엔티티자체에 가상컬럼 매핑

    public int findCommentLikeNum(Long commentId) {
        int size = jpaQueryFactory.selectFrom(commentLike)
            .where(commentLike.boardComment.id.eq(commentId))
            .fetch().size();

        return size;
    }




}
