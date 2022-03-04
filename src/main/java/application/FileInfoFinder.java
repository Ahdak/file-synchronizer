package application;

import domain.FileCopyInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record FileInfoFinder(String sourceFolderPath, String destFolderPath) {

    List<FileCopyInfo> listFileInfoToCopy() throws IOException {
        List<FileCopyInfo> result = new ArrayList<>();
        listFileInfoToCopy(Paths.get(sourceFolderPath), result);
        return result;
    }

    private void listFileInfoToCopy(Path input, List<FileCopyInfo> result) throws IOException {
        var sources = Files.list(input)
                .map(this::createInfo)
                .collect(Collectors.toList());
        for (var fileCopyInfo : sources) {
            if (Files.isDirectory(fileCopyInfo.dest())) {
                if (Files.exists(fileCopyInfo.dest())) {
                    listFileInfoToCopy(fileCopyInfo.src(), result);
                } else {
                    result.add(fileCopyInfo);
                }
            } else {
                if (!Files.exists(fileCopyInfo.dest())) {
                    result.add(fileCopyInfo);
                }
            }
        }
    }

    private FileCopyInfo createInfo(Path path) {
        return new FileCopyInfo(path, Paths.get(path.toString().replace(sourceFolderPath, destFolderPath)));
    }
}
