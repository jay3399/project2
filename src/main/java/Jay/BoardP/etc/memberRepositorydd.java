package Jay.BoardP.etc;


import Jay.BoardP.controller.form.LoginForm;
import Jay.BoardP.domain.Member;
import Jay.BoardP.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class memberRepositorydd {
    private final EntityManager em;

    private final SpringDataMemberRepository repository;

    private final JPAQueryFactory jpaQueryFactory;

    public memberRepositorydd(EntityManager em, SpringDataMemberRepository repository) {
        this.em = em;
        this.repository = repository;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Long addMember(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findOne(Long id) {
        Member member = em.find(Member.class, id);
        return member;
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Member findByUserId(String userId) {
        Optional<Member> memberByUserId = repository.findMemberByUserId(userId);
        Member member = memberByUserId.orElse(null);

        return member;
    }

    public Member findByEmail(String email) {
        Member memberByEmail = repository.findMemberByEmail(email);
        return memberByEmail;
    }

    public Member Login(LoginForm form) {
        QMember member = new QMember("m");

        Member member1 = jpaQueryFactory.selectFrom(member)
            .where(member.userId.eq(form.getUserId()).and(member.password.eq(form.getPassword()))).fetchOne();


        return member1;

    }

//    public List<Member> getMembers() {
//        em.createQuery("select m from Member m where m.")
//    }


//    public Member findByUserId(String userId) {
//        return em.createQuery("select m from Member m where m.userId = : userId", Member.class)
//            .setParameter("userId", userId)
//            .getSingleResult();
//    }

}
