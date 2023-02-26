package Jay.BoardP.repository;

import static Jay.BoardP.domain.QPenalty.penalty;

import Jay.BoardP.domain.Penalty;
import Jay.BoardP.repository.customInter.PenaltyCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class PenaltyRepositoryImpl implements PenaltyCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public PenaltyRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Penalty> exist(Long memberId, Long boardId) {
        Penalty penalty1 = jpaQueryFactory.selectFrom(penalty)
            .where(penalty.board.id.eq(boardId), penalty.member.id.eq(memberId)).fetchFirst();

        return Optional.ofNullable(penalty1);
    }


}
