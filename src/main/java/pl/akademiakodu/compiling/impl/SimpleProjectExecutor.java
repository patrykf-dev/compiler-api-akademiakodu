package pl.akademiakodu.compiling.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.models.JavaProject;
import pl.akademiakodu.compiling.ProjectExecutor;
import pl.akademiakodu.services.FileStorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class SimpleProjectExecutor implements ProjectExecutor {
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * This method needs to create separate process, because it's the only way to create new JVM. Using the current
     * JVM and its System.out leads to Spring logging into the output file, which of course is not intended.
     */
    public Path runProject(JavaProject javaProject) throws IOException, InterruptedException {
        File outputFile = fileStorageService.createOutputFile(javaProject);
        File errorFile = fileStorageService.createErrorFile(javaProject);

        String args[] = {"java", "-classpath", javaProject.getClassPath(), javaProject.getMainClassFullName()};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.redirectOutput(outputFile);
        processBuilder.redirectError(errorFile);
        Process p = processBuilder.start();
        p.waitFor();

        return outputFile.toPath();
    }
}
