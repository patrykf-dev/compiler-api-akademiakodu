package pl.akademiakodu.compiling;

import pl.akademiakodu.compiling.process.ProcessExecutionResults;

import java.io.IOException;

public interface ProjectExecutor {
    ProcessExecutionResults runProject(ProjectDetails projectDetails) throws IOException, InterruptedException;
}
