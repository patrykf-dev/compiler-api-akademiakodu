package pl.akademiakodu.compiling;

import java.io.IOException;


public interface ProjectCompiler {
    ProcessExecutionResults compileProject(JavaProject javaProject) throws IOException, InterruptedException;
}
