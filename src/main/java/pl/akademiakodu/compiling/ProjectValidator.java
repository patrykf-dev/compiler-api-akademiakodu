package pl.akademiakodu.compiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.models.JavaProject;
import pl.akademiakodu.services.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class ProjectValidator {

    @Autowired
    private ProjectCompiler compiler;
    @Autowired
    private ProjectExecutor executor;


    public ProjectValidationResult validateProject(JavaProject javaProject, String expectedResult) {
        try {
            compiler.compileProject(javaProject);
        } catch (Exception e) {
            e.printStackTrace();
            return ProjectValidationResult.COMPILATION_ERROR;
        }

        Path output;
        try {
            output = executor.runProject(javaProject);
        } catch (Exception e) {
            e.printStackTrace();
            return ProjectValidationResult.EXECUTION_ERROR;
        }


        StringBuilder compilationOutput = new StringBuilder();
        try {
            Stream<String> stream = Files.lines(output);
            stream.forEach(s -> compilationOutput.append(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (compilationOutput.toString().equals(expectedResult)) {
            return ProjectValidationResult.VALID_RESULT;
        } else {
            return ProjectValidationResult.INVALID_RESULT;
        }
    }
}
