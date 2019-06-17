package pl.akademiakodu.compiling;

import java.io.IOException;

public interface ProjectExecutor {
    ProcessExecutionResults runProject(ProjectDetails projectDetails) throws IOException, InterruptedException;
}
