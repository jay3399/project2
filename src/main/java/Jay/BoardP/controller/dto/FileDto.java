package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.FileType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {

    Long id;

    private String originalFilename;
    private String storedFilename;

    private FileType fileType;

    public FileDto() {

    }

    @QueryProjection
    public FileDto(String originalFilename , String storedFilename , FileType fileType) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.fileType = fileType;
    }

//    public static FileDto createFileDto(File file) {
//        FileDto fileDto  = new FileDto();
//        fileDto.set(file);
//        return fileDto;
//    }


}
