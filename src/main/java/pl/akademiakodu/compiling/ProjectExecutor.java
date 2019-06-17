package pl.akademiakodu.compiling;

import java.io.IOException;

public interface ProjectExecutor {
    ProcessExecutionResults runProject(JavaProject javaProject) throws IOException, InterruptedException;
}
