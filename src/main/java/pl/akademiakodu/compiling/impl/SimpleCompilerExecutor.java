package pl.akademiakodu.compiling.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
public class SimpleCompilerExecutor implements CompilerExecutor {
    @Autowired
    private FileStorageService fileStorageService;


    public Path compileSource(Path javaFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());
        String path = javaFile.getParent().resolve(javaFile + ".class").toString().replace(".java", "");
        return Paths.get(path);
    }

    /**
     * This method needs to create another process, beacues it's the only way to create new JVM. Using the current
     * JVM and its System.out leads to Spring logging into the ouput file, which of course is not intended.
     * @param javaClass path to java class to execute
     * @return path to the output file
     */
    public Path runClass(Path javaClass) throws Exception {
        try {

            String outputPath = fileStorageService.getOutputPathForFile();
            System.out.println("javaClass NAME IS " + javaClass.toString());

            String mainClassFolderPath = "D:\\Development\\compiler-api-akademiakodu-results\\";
            String mainClassName = "Hello";

            String cmd[] = {"java", "-cp", mainClassFolderPath, mainClassName};
            ProcessBuilder pb = new ProcessBuilder(cmd);
            File outputFile = new File(outputPath);
            pb.redirectOutput(outputFile);
            Process p = pb.start();
            p.waitFor();
            return Paths.get(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
