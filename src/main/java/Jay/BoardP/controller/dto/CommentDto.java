package Jay.BoardP.controller.dto;


import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {


    @NotBlank(message = "내용을 입력해주세요.")
    private String content;


//    public BoardComment createBoardComment(Member member, Board board) {
//        return BoardComment.createComment(board, member, content);
//    }
//
//




}
