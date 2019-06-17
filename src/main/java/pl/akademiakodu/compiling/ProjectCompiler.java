package pl.akademiakodu.compiling;

import java.io.IOException;


public interface ProjectCompiler {
    ProcessExecutionResults compileProject(ProjectDetails projectDetails) throws IOException, InterruptedException;
}
