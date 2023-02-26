package Jay.BoardP.etc;


import static Jay.BoardP.domain.QPostLike.postLike;

import Jay.BoardP.domain.PostLike;
import Jay.BoardP.domain.QPostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostLikeRepositorydd {

    private final JPAQueryFactory jpaQueryFactory;
    private final SpringDataPostLikeRepository repository;

    @Autowired
    public PostLikeRepositorydd(EntityManager em, SpringDataPostLikeRepository repository) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.repository = repository;
    }

    public Optional<PostLike> exist(Long memberId, Long boardId) {
        QPostLike qPostLike = new QPostLike("q");

        PostLike postLike = jpaQueryFactory.selectFrom(qPostLike)
            .where(qPostLike.member.id.eq(memberId), qPostLike.board.id.eq(boardId)).fetchFirst();

        System.out.println("postLike = " + postLike);

        return Optional.ofNullable(postLike);

    }

    public Long findPostLikeNum(Long boardId) {
        long l = jpaQueryFactory.selectFrom(postLike).where(postLike.board.id.eq(boardId))
            .fetchCount();
        return l;
    }





}
