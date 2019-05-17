package pl.akademiakodu.compiling;

import pl.akademiakodu.models.JavaProject;

import java.io.IOException;
import java.nio.file.Path;

public interface ProjectExecutor {
    Path runProject(JavaProject javaProject) throws IOException, InterruptedException;
}
