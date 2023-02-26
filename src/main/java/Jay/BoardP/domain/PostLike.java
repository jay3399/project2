package Jay.BoardP.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public void setMember(Member member) {
        this.member = member;
        member.getPostLikes().add(this);
    }

    public void setBoard(Board board) {
        this.board  = board;
        board.getPostLikes().add(this);
    }


    public void removeMember(Member member , Board board) {
        member.getPostLikes().remove(this);
        board.getPostLikes().remove(this);
    }


    public static PostLike createPostLike(Member member , Board board ) {
        PostLike postLike = new PostLike();
        postLike.setMember(member);
        postLike.setBoard(board);
        return postLike;
    }


}
