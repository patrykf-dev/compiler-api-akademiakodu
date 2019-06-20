package pl.akademiakodu.compiling;

import pl.akademiakodu.compiling.process.ProcessExecutionResults;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public interface ProjectCompiler {
    ProcessExecutionResults compileProject(ProjectDetails projectDetails) throws IOException, InterruptedException, TimeoutException;
}
