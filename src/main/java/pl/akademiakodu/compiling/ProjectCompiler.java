package pl.akademiakodu.compiling;

import pl.akademiakodu.compiling.process.ProcessExecutionResults;

import java.io.IOException;


public interface ProjectCompiler {
    ProcessExecutionResults compileProject(ProjectDetails projectDetails) throws IOException, InterruptedException;
}
