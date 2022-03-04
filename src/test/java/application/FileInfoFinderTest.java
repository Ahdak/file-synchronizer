package application;

import domain.FileCopyInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

class FileInfoFinderTest {

    @Test
    void find_missing_file_and_folder() throws IOException {
        var items = new FileInfoFinder("src/test/resources/source","src/test/resources/dest").listFileInfoToCopy() ;
        Assertions.assertThat(items).containsExactlyInAnyOrder(
                new FileCopyInfo(Path.of("src/test/resources/source/folder1/f1.txt"),Path.of("src/test/resources/dest/folder1/f1.txt")),
                new FileCopyInfo(Path.of("src/test/resources/source/folder1/folder11"),Path.of("src/test/resources/dest/folder1/folder11")),
                new FileCopyInfo(Path.of("src/test/resources/source/folder2"),Path.of("src/test/resources/dest/folder2"))
        ) ;
    }

}