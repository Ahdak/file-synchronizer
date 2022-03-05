package application;

import domain.FileCopyInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

class CopyExecutorTest {

    @TempDir
    Path srcPath ;

    @TempDir
    Path destPath ;

    @Test
    void execute_copy() throws IOException, InterruptedException, ExecutionException {
        // SRC
        var d1Src = srcPath.resolve("d1") ;
        var d1f1Src = d1Src.resolve("f11.txt") ;
        var d1f2Src = d1Src.resolve("f2.txt") ;
        var f1Src = srcPath.resolve("f1.txt") ;
        Files.createDirectories(d1Src) ;
        Files.createFile(d1f1Src) ;
        Files.createFile(d1f2Src) ;
        Files.createFile(f1Src) ;
        // DEST
        var d1dest = destPath.resolve("d1") ;
        var f1Dest = destPath.resolve("f1.txt") ;
        // When
        var res = new CopyExecutor().executeCopy(List.of(
                new FileCopyInfo(d1Src,d1dest),
                new FileCopyInfo(f1Src,f1Dest)
        ));
        // then
        List<Path> copiedFiles = getAllFilesInDest(destPath) ;
        Assertions.assertThat(copiedFiles).containsExactlyInAnyOrder(
                d1dest,
                d1dest.resolve("f11.txt"),
                d1dest.resolve("f2.txt"),
            destPath.resolve("f1.txt")
        );
        Assertions.assertThat(res).containsExactlyInAnyOrder(
                d1dest,
                destPath.resolve("f1.txt")
        );
    }

    private List<Path> getAllFilesInDest(Path destPath) throws IOException {
        List<Path> result = new ArrayList<>() ;
        Queue<Path> queue = new ArrayDeque<>() ;
        queue.add(destPath) ;
        while (!queue.isEmpty()) {
            var current = queue.poll() ;
            var content = Files.list(current).collect(Collectors.toList()) ;
            for (var c : content) {
                result.add(c) ;
                if (Files.isDirectory(c)) {
                    queue.add(c) ;
                }
            }
        }
        return result;
    }

}