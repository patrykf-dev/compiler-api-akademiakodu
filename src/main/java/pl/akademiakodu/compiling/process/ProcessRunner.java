package pl.akademiakodu.compiling.process;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ProcessRunner {

    private static final int TIMEOUT = 30;

    /**
     * This method needs to create separate process, because it's the only way to create new JVM. Using the current
     * JVM and its System.out leads to Spring logging into the output file, which of course is not intended.
     */
    public boolean runProcess(String[] args, File outputFile, File errorFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.redirectOutput(outputFile);
        processBuilder.redirectError(errorFile);
        Process p = processBuilder.start();
        return p.waitFor(TIMEOUT, TimeUnit.SECONDS);
    }
}
