package application;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IntegrationTest {

    @Test
    void synchro_multimedia() throws IOException {
        String src = "/Users/ahmeddammak/Documents/MULTIMEDIA" ;
        String dest = "/Volumes/Maxtor/MULTIMEDIA" ;
        // check files to copy
        var filesToCopy = new FileInfoFinder(src,dest).listFileInfoToCopy() ;
        filesToCopy.forEach(System.out::println);
        //new CopyExecutor().executeCopy(filesToCopy) ;
    }
}
