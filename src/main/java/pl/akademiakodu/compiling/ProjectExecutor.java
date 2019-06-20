package pl.akademiakodu.compiling;

import pl.akademiakodu.compiling.process.ProcessExecutionResults;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface ProjectExecutor {
    ProcessExecutionResults runProject(ProjectDetails projectDetails) throws IOException, InterruptedException, TimeoutException;
}
