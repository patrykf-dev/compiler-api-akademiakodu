package pl.akademiakodu.compiling.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.compiling.ProjectDetails;
import pl.akademiakodu.compiling.process.ProcessExecutionResults;
import pl.akademiakodu.compiling.process.ProcessRunner;
import pl.akademiakodu.compiling.ProjectExecutor;
import pl.akademiakodu.services.FileStorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SimpleProjectExecutor implements ProjectExecutor {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ProcessRunner processRunner;


    public ProcessExecutionResults runProject(ProjectDetails projectDetails) throws IOException, InterruptedException {
        File outputFile = fileStorageService.createExecutionOutputFile(projectDetails);
        File errorFile = fileStorageService.createExecutionErrorFile(projectDetails);

        String args[] = {"java", "-classpath", projectDetails.getClassPath(), projectDetails.getMainClassFullName()};
        processRunner.runProcess(args, outputFile, errorFile);

        Path out = Paths.get(outputFile.getAbsolutePath());
        Path err = Paths.get(errorFile.getAbsolutePath());
        return new ProcessExecutionResults(out, err);
    }
}
