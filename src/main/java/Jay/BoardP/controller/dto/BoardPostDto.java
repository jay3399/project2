package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.FileType;
import Jay.BoardP.domain.Member;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPostDto {

    private String nickName;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String ipAddress;

    private String categoryCode;


    private Map<FileType, List<MultipartFile>> attachmentFiles = new ConcurrentHashMap<>();

    @Builder
    public BoardPostDto(String nickName, String title, String content,
        Map<FileType, List<MultipartFile>> attachmentFiles , String ipAddress , String categoryCode) {
        this.nickName = nickName;
        this.title = title;
        this.attachmentFiles = attachmentFiles;
        this.ipAddress = ipAddress;
        this.content = content;
        this.categoryCode = categoryCode;
    }

//    public static BoardPostDto createBoardPostDto(String nickName, String title, String content,
//        Map<FileType, List<MultipartFile>> attachmentFiles) {
//        BoardPostDto boardPostDto = new BoardPostDto();
//        boardPostDto.setTitle(title);
//        boardPostDto.setContent(content);
//        boardPostDto.setAttachmentFiles(attachmentFiles);
//        return boardPostDto;
//    }


//    public static Board createWriteBoard(Member member, BoardPostDto dto ) {
//        Board board = Board.createBoard(dto.title, dto.getContent(), dto.ipAddress, member , dto.getCategoryCode());
//        return board;
//    }

    public Board createBoard(Member member) {
        return Board.createBoard(title, content, ipAddress, member, categoryCode);
    }

//    public static Board createWriteBoar
//    dV2(Member member, BoardPostDto dto ) {
//        Board board = Board.createBoard(member.getNickname(), dto.getTitle(), dto.getContent() ,
//            dto.getIpAddress() , dto.getCategory());
//        return board;
//    }


}

