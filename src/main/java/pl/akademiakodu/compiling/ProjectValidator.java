package pl.akademiakodu.compiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.compiling.process.ProcessExecutionResults;
import pl.akademiakodu.models.UploadedProject;

import java.util.concurrent.TimeoutException;

@Component
public class ProjectValidator {
    @Autowired
    private ProjectCompiler compiler;
    @Autowired
    private ProjectExecutor executor;


    public ProjectValidationResult runProject(UploadedProject uploadedProject) {
        ProjectValidationResult compilationErrors = compileProject(uploadedProject);
        if(compilationErrors != null)
            return compilationErrors;

        ProjectValidationResult executionErrors = executeProject(uploadedProject);
        if(executionErrors != null)
            return executionErrors;

        return null;
    }

    public ProjectValidationResult verifyOutput(String userOutput, String expectedOutput) {
        if (outputsEqual(userOutput, expectedOutput)) {
            return ProjectValidationResult.VALID_RESULT;
        } else {
            return ProjectValidationResult.INVALID_RESULT;
        }
    }

    private ProjectValidationResult compileProject(UploadedProject uploadedProject) {
        try {
            ProcessExecutionResults results = compiler.compileProject(uploadedProject.getProjectDetails());
            String out = results.getOutputFile().toAbsolutePath().toString();
            String err = results.getErrorFile().toAbsolutePath().toString();
            uploadedProject.setCompilationOutPath(out);
            uploadedProject.setCompilationErrorPath(err);
        } catch (TimeoutException te) {
            te.printStackTrace();
            return ProjectValidationResult.COMPILATION_TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
            return ProjectValidationResult.COMPILATION_ERROR;
        }
        return null;
    }

    private ProjectValidationResult executeProject(UploadedProject uploadedProject) {
        try {
            ProcessExecutionResults results = executor.runProject(uploadedProject.getProjectDetails());
            String out = results.getOutputFile().toAbsolutePath().toString();
            String err = results.getErrorFile().toAbsolutePath().toString();
            uploadedProject.setExecutionOutPath(out);
            uploadedProject.setExecutionErrorPath(err);
        } catch (TimeoutException te) {
            te.printStackTrace();
            return ProjectValidationResult.EXECUTION_TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
            return ProjectValidationResult.EXECUTION_ERROR;
        }
        return null;
    }

    private boolean outputsEqual(String userOutput, String expectedOutput) {
        String userConverted = userOutput.trim();
        String expectedConverted = expectedOutput.trim();
        return userConverted.equalsIgnoreCase(expectedConverted);
    }
}
