package Jay.BoardP.etc;

import static Jay.BoardP.domain.QBoardComment.boardComment;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.domain.QBoardComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
//@RequiredArgsConstructor
public class CommentRepositorydd {

    private final EntityManager em ;
    private final JPAQueryFactory queryFactory;

    private final SpringDataCommRepository dataCommRepository;

    @Autowired
    public CommentRepositorydd(EntityManager em , SpringDataCommRepository springDataCommRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.dataCommRepository = springDataCommRepository;
    }

    public Long save(BoardComment comment) {
        em.persist(comment);
        return comment.getId();
    }

    public BoardComment findById(Long id) {
        return em.find(BoardComment.class, id);
    }

    public List<BoardComment> findByBoardId(Long boardId) {
        Board board = em.find(Board.class, boardId);
        return board.getComments();
    }

    public List<BoardComment> findByBoardIdV2(Long boardId) {
        QBoardComment qBoardComment = new QBoardComment("q");
        List<BoardComment> fetch = queryFactory.selectFrom(qBoardComment)
            .leftJoin(qBoardComment.parent).fetchJoin()
            .where(qBoardComment.board.id.eq(boardId))
            .orderBy(qBoardComment.parent.id.asc().nullsFirst(), qBoardComment.createdDate.asc()
            ).fetch();
        return fetch;
    }

    public Long totalComments(Long boardId) {


        int size = queryFactory.selectFrom(boardComment).where(boardComment.board.id.eq(boardId))
            .fetch()
            .size();

        return (long) size;
    }


    public Optional<BoardComment> findByIdWithParent(Long commentId) {

        String jpql = "select c from BoardComment c left join fetch c.parent where c.id=:id";

        BoardComment id = (BoardComment) em.createQuery(jpql).setParameter("id", commentId).getSingleResult();

        return Optional.of(id);

    }


    public void delete(Long commentId) {
        dataCommRepository.deleteById(commentId);
    }



}
