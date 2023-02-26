package Jay.BoardP.domain;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String storedFilename;

    @Enumerated(EnumType.STRING)
    private FileType fileType;


    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    //@ManyToOne(fetch = FetchType.LAZY)
//    }


    @Builder
    public File(Long id, String originalFilename, String storedPath, FileType fileType,
        Board board) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedPath;
        this.fileType = fileType;
        this.board = board;
        board.getFiles().add(this);
    }


    public void setFile(Long id, String originalFilename, String storedPath, FileType fileType,
        Board board) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedPath;
        this.fileType = fileType;
        this.board = board;
    }


    public static File createFile(
        Long id, String originalFilename, String storedPath, FileType fileType, Board board
    ) {
        File file = new File();
        file.setFile(id, originalFilename, storedPath, fileType, board);
        return file;
    }


}
