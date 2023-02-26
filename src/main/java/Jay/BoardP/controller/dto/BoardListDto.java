package Jay.BoardP.controller.dto;


import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.File;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BoardListDto {

    private Long id;
    private String title;
    private String createBy;
    private LocalDateTime localDateTime;

    private Long countVisit;
    private Long postLike;

    private Long totalComments;

    private File mainImage;

    private String ipAddress;

    private String categoryName;

    private Boolean isDeleted;


    private BoardListDto() {

    }
//
//    public void setTotalComments(Long comments) {
//        this.totalComments = comments;
//    }

    public void set(Board board) {
        this.id = board.getId();
        this.title =board.getTitle();
        this.createBy = board.getCreateBy();
        this.localDateTime = board.getCreatedDate();
        this.countVisit = board.getCountVisit();
        this.isDeleted = board.getIsDeleted();
        this.postLike = Long.valueOf(board.getCountOfLikes());
        this.totalComments = Long.valueOf(board.getCountOfComments());
//        this.postLike = (long) board.getPostLikes().size();
        this.mainImage =
            (board.getFiles().size() != 0) ? board.getFiles()
                .get(0) : null;
        this.ipAddress = board.getIpAddress();
        this.categoryName = (board.getCategory().getCode().equals("FREE")) ? "잡담" :
            (board.getCategory().getCode().equals("QNA")) ? "질문" :
                (board.getCategory().getCode().equals("FUN")) ? "유머" :
                    (board.getCategory().getCode().equals("NOTICE")) ? "공지사항" : null;

    }


//    public void set(Board board) {
//        this.postLike = (long) board.getPostLikes().size();
//        this.mainImage =
//            (board.getFiles().size() != 0) ? board.getFiles()
//                .get(0) : null;
//        this.ipAddress = board.getIpAddress();
//        this.categoryName = (board.getCategory().getCode().equals("FREE")) ? "잡담" :
//            (board.getCategory().getCode().equals("QNA")) ? "질문" :
//                (board.getCategory().getCode().equals("FUN")) ? "유머" : "ETC";
//
//    }
//
//    public void setTotalComments(Long comments) {
//        this.totalComments = comments;
//    }


    public static BoardListDto from(Board board) {
        BoardListDto boardListDto = new BoardListDto();
        boardListDto.set(board);
        return boardListDto;
    }


}




