package Jay.BoardP.service;

import Jay.BoardP.controller.dto.FileStore;
import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.File;
import Jay.BoardP.domain.FileType;
import Jay.BoardP.repository.FileRepository;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;

    public List<File> saveFiles(Map<FileType, List<MultipartFile>> fileListMap , Board board) throws IOException {
        List<File> imageFiles = fileStore.storeFiles(fileListMap.get(FileType.IMAGE),
            FileType.IMAGE , board);

        for (File imageFile : imageFiles) {
            System.out.println("imageFile.getOriginalFilename( = " + imageFile.getOriginalFilename());
        }

        List<File> generalFiles = fileStore.storeFiles(fileListMap.get(FileType.GENERAL),
            FileType.GENERAL , board);

        List<File> collect = Stream.of(imageFiles, generalFiles)
            .flatMap(Collection::stream).collect(Collectors.toList());

        for (File file : collect) {
            System.out.println("file = " + file);
        }

        return collect;
    }


    public Map<FileType, List<File>> findFiles() {
        List<File> files = fileRepository.findAll();

        return files.stream()
            .collect(Collectors.groupingBy(File::getFileType));
    }


}
