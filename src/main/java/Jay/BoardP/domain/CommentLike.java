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
public class CommentLike {

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
        member.getCommentLikeList().add(this);
    }

    public void setBoardComment(BoardComment boardComment) {
        this.boardComment = boardComment;
        boardComment.getLikes().add(this);
    }

    public void removeComment(BoardComment comment, Member member) {
        comment.getLikes().remove(this);
        member.getCommentLikeList().remove(this);
    }

    public static CommentLike createCommentLike(BoardComment comment, Member member) {
        CommentLike commentLike = new CommentLike();
        commentLike.setMember(member);
        commentLike.setBoardComment(comment);
        return commentLike;
    }
}
