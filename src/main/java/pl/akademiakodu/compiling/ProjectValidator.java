package pl.akademiakodu.compiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.compiling.process.ProcessExecutionResults;
import pl.akademiakodu.models.UploadedProject;

@Component
public class ProjectValidator {

    @Autowired
    private ProjectCompiler compiler;
    @Autowired
    private ProjectExecutor executor;


    public ProjectValidationResult runProject(UploadedProject uploadedProject) {
        if (!compileProject(uploadedProject)) return ProjectValidationResult.COMPILATION_ERROR;
        if (!executeProject(uploadedProject)) return ProjectValidationResult.EXECUTION_ERROR;
        return null;
    }

    public ProjectValidationResult verifyOutput(String userOutput, String expectedOutput) {
        if (outputsEqual(userOutput, expectedOutput)) {
            return ProjectValidationResult.VALID_RESULT;
        } else {
            return ProjectValidationResult.INVALID_RESULT;
        }
    }

    private boolean outputsEqual(String userOutput, String expectedOutput) {
        String userConverted = userOutput.trim().toLowerCase();
        String expectedConverted = expectedOutput.trim().toLowerCase();
        return userConverted.equals(expectedConverted);
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
