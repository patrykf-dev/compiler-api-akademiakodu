package pl.akademiakodu.compiling.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.compiling.JavaProject;
import pl.akademiakodu.compiling.ProcessExecutionResults;
import pl.akademiakodu.compiling.ProcessRunner;
import pl.akademiakodu.compiling.ProjectCompiler;
import pl.akademiakodu.services.FileStorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SimpleProjectCompiler implements ProjectCompiler {

    @Autowired
    private ProcessRunner processRunner;
    @Autowired
    private FileStorageService fileStorageService;

    public ProcessExecutionResults compileProject(JavaProject javaProject) throws IOException, InterruptedException {
        File outputFile = fileStorageService.createCompilationOutputFile(javaProject);
        File errorFile = fileStorageService.createCompilationErrorFile(javaProject);

        String args[] = {"javac", "-sourcepath", javaProject.getSourcePath(), javaProject.getMainClassPath(),
                "-d", javaProject.getClassPath()};
        processRunner.runProcess(args, outputFile, errorFile);
        Path out = Paths.get(outputFile.getAbsolutePath());
        Path err = Paths.get(errorFile.getAbsolutePath());
        return new ProcessExecutionResults(out, err);
    }
}
