package Jay.BoardP.etc;


import static Jay.BoardP.domain.QPenalty.penalty;

import Jay.BoardP.domain.Penalty;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PenaltyRepositorydd {

    private final JPAQueryFactory jpaQueryFactory;
    private final SpringDataPenaltyRepository repository;

    @Autowired
    public PenaltyRepositorydd(EntityManager entityManager, SpringDataPenaltyRepository repository) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.repository = repository;
    }

    public Optional<Penalty> exist(Long memberId, Long boardId) {
        Penalty penalty1 = jpaQueryFactory.selectFrom(penalty)
            .where(penalty.board.id.eq(boardId), penalty.member.id.eq(memberId)).fetchFirst();

        return Optional.ofNullable(penalty1);
    }

    public List<Penalty> getPenalties(Long boardId) {
        List<Penalty> allByBoardId = repository.findAllByBoardId(boardId);
        return repository.findAllByBoardId(boardId);
    }









}
