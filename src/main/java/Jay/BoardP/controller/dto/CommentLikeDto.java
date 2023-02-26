package Jay.BoardP.controller.dto;


import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeDto {

    private Member member;

    private BoardComment boardComment;

}
