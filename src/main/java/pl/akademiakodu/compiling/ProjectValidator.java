package pl.akademiakodu.compiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.models.UploadedProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class ProjectValidator {

    @Autowired
    private ProjectCompiler compiler;
    @Autowired
    private ProjectExecutor executor;


    public ProjectValidationResult validateProject(UploadedProject uploadedProject, String expectedResult) {
        if (!compileProject(uploadedProject)) return ProjectValidationResult.COMPILATION_ERROR;
        if (!executeProject(uploadedProject)) return ProjectValidationResult.EXECUTION_ERROR;

        Path output = Paths.get(uploadedProject.getExecutionOutPath());

        return verifyOutput(expectedResult, output);
    }

    private ProjectValidationResult verifyOutput(String expectedResult, Path output) {
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

    private boolean compileProject(UploadedProject uploadedProject) {
        try {
            ProcessExecutionResults results = compiler.compileProject(uploadedProject.getProjectDetails());
            String out = results.getOutputFile().toAbsolutePath().toString();
            String err = results.getErrorFile().toAbsolutePath().toString();
            uploadedProject.setCompilationOutPath(out);
            uploadedProject.setCompilationErrorPath(err);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean executeProject(UploadedProject uploadedProject) {
        try {
            ProcessExecutionResults results = executor.runProject(uploadedProject.getProjectDetails());
            String out = results.getOutputFile().toAbsolutePath().toString();
            String err = results.getErrorFile().toAbsolutePath().toString();
            uploadedProject.setExecutionOutPath(out);
            uploadedProject.setExecutionErrorPath(err);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
