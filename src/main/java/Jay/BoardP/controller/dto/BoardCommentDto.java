package Jay.BoardP.controller.dto;


import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.domain.DeleteStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentDto{

    private Long id;
    private String nickName;
    private String content;

    private Long parentId;
    private Long likeNum;
    private Long disLikeNum;

    private DeleteStatus deleteStatus;

    private List<BoardCommentDto> child = new ArrayList<>();


    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;



    public void setBoardComment(BoardComment boardComment) {
        this.id = boardComment.getId();
        this.nickName = boardComment.getNickName();
        this.content = boardComment.getContent();
        this.createdDate = boardComment.getCreatedDate();
        this.likeNum = boardComment.getCountsOfLike();
        this.disLikeNum = boardComment.getCountsOfDisLike();
//        this.createdDate = boardComment.getCreatedDate();
        this.deleteStatus = boardComment.getIsDeleted();

//        int size = boardComment.getDisLikes().size();
//        // 사이즈로 가져오면 계속해서 쿼리를 날린다
//
    }

    public static BoardCommentDto fromEntity(BoardComment boardComment) {
        BoardCommentDto dto = new BoardCommentDto();
        dto.setBoardComment(boardComment);
        return dto;
    }


}
