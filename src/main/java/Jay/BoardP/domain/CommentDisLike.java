package Jay.BoardP.domain;


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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentDisLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_coment_id")
    private BoardComment boardComment;


    public void setMember(Member member) {
        this.member = member;
        member.getCommentDisLikeList().add(this);
    }

    public void setBoardComment(BoardComment boardComment) {
        this.boardComment = boardComment;
        boardComment.getDisLikes().add(this);
    }

    public void removeComment(BoardComment boardComment, Member member) {
        boardComment.getDisLikes().remove(this);
        member.getCommentDisLikeList().remove(this);
    }

    public static CommentDisLike createCommentDisLike(BoardComment comment, Member member) {
        CommentDisLike dislike = new CommentDisLike();
        dislike.setMember(member);
        dislike.setBoardComment(comment);
        return dislike;
    }


}
