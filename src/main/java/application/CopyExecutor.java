package application;

import domain.FileCopyInfo;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Log
public class CopyExecutor {

    public List<Path> executeCopy(List<FileCopyInfo> files) throws InterruptedException, ExecutionException {
        List<Path> result = new ArrayList<>() ;
        var executor = Executors.newFixedThreadPool(8) ;
        var task = files.stream().map(this::createCallable).collect(Collectors.toList());
        var futures = executor.invokeAll(task) ;
        for (var f : futures) {
            if (f.isDone()) {
                result.add(f.get()) ;
            } else {
                log.severe("Error occured when copy");
            }
        }
        return result ;
    }

    private Callable<Path> createCallable(FileCopyInfo fileCopyInfo) {
        return () -> {
            if (Files.isDirectory(fileCopyInfo.src())) {
                log.info("Copying folder " +fileCopyInfo.src()+ " => "+fileCopyInfo.dest() );
                FileUtils.copyDirectory(fileCopyInfo.src().toFile(),fileCopyInfo.dest().toFile());
            } else {
                log.info("Copying file " +fileCopyInfo.src()+ " => "+fileCopyInfo.dest() );
                Files.copy(fileCopyInfo.src(),fileCopyInfo.dest())  ;
            }
            return fileCopyInfo.dest() ;
        } ;
    }
}
