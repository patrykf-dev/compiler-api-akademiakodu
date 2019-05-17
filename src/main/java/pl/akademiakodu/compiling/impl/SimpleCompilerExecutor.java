package pl.akademiakodu.compiling.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.JavaProject;
import pl.akademiakodu.compiling.CompilerExecutor;
import pl.akademiakodu.services.FileStorageService;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class SimpleCompilerExecutor implements CompilerExecutor {
    @Autowired
    private FileStorageService fileStorageService;


    public Path compileProject(JavaProject javaProject) {
        String cmd[] = {"javac", "-sourcepath", javaProject.getSourcePath(), javaProject.getMainClassPath()};
        System.out.println("Cmd args for compiling: " + Arrays.toString(cmd));

        ProcessBuilder pb = new ProcessBuilder(cmd);
        try {
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Path mainPath = Paths.get(javaProject.getMainClassPath());
        String path = mainPath.getParent().resolve(mainPath + ".class").toString().replace(".java", "");
        return Paths.get(path);
    }

    /**
     * This method needs to create separate process, because it's the only way to create new JVM. Using the current
     * JVM and its System.out leads to Spring logging into the output file, which of course is not intended.
     *
     * @param javaClass path to java class to execute
     * @return path to the output file
     */
    public Path runProject(JavaProject javaProject) {
        try {
            String outputPath = fileStorageService.getOutputPathForFile();
            String errorPath = fileStorageService.getErrorPathForFile();
            String cmd[] = {"java", "-classpath", javaProject.getSourcePath(), javaProject.getMainClassFullName()};
            System.out.println("Cmd args for executing: " + Arrays.toString(cmd));
            ProcessBuilder pb = new ProcessBuilder(cmd);
            File outputFile = new File(outputPath);
            File errorFile = new File(errorPath);
            pb.redirectOutput(outputFile);
            pb.redirectError(errorFile);
            Process p = pb.start();
            p.waitFor();
            return Paths.get(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
