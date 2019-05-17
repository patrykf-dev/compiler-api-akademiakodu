package pl.akademiakodu.compiling.impl;

import org.springframework.stereotype.Component;
import pl.akademiakodu.models.JavaProject;
import pl.akademiakodu.compiling.ProjectCompiler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SimpleProjectCompiler implements ProjectCompiler {

    public void compileProject(JavaProject javaProject) throws IOException, InterruptedException {
        String args[] = {"javac", "-sourcepath", javaProject.getSourcePath(), javaProject.getMainClassPath()};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process p = processBuilder.start();
        p.waitFor();
        Path mainPath = Paths.get(javaProject.getMainClassPath());
        String path = mainPath.getParent().resolve(mainPath + ".class").toString().replace(".java", "");
    }
}
