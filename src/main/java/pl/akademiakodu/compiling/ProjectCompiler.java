package pl.akademiakodu.compiling;

import pl.akademiakodu.models.JavaProject;

import java.io.IOException;
import java.nio.file.Path;


public interface ProjectCompiler {
    void compileProject(JavaProject javaProject) throws IOException, InterruptedException;
}
