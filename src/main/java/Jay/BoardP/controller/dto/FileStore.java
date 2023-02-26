package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.File;
import Jay.BoardP.domain.FileType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public class FileStore {

    @Value("${file.dir}/")
    private String fileDirPath;

    private String extract(String originalFilename) {
        int i = originalFilename.lastIndexOf(".");
        return originalFilename.substring(i);
    }

    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String extract = extract(originalFilename);
        return uuid + extract;
    }


    private String createPath(String storeFilename, FileType fileType) {
        String viaPath = (fileType == FileType.IMAGE) ? "images/" : "generals/";
        return fileDirPath + viaPath + storeFilename;
    }


    public File storeFile(MultipartFile multipartFile , FileType fileType , Board board) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        System.out.println("originalFilename = " + originalFilename);
        String storeFilename = createStoreFilename(originalFilename);
        System.out.println("storeFilename = " + storeFilename);
        multipartFile.transferTo(new java.io.File(createPath(storeFilename, fileType)));

        return File.builder()
            .originalFilename(originalFilename)
            .storedPath(storeFilename)
            .fileType(fileType)
            .board(board)
            .build();
    }

    public List<File> storeFiles(List<MultipartFile> multipartFiles, FileType fileType , Board board)
        throws IOException {
        List<File> files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                files.add(storeFile(multipartFile, fileType ,board ));
            }
        }
        return files;
    }



}
