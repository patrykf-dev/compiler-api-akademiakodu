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

    public Path runClass(Path javaClass) throws Exception {
        URL classUrl = javaClass.getParent().toFile().toURI().toURL();
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
        Class<?> clazz = Class.forName("Hello", true, classLoader);
        clazz.getConstructor().newInstance();
        Method method = clazz.getMethod("main", String[].class);
        try {
            String outputPath = fileStorageService.getOutputPathForFile();
            PrintStream fileStream = new PrintStream(outputPath);
            System.setOut(fileStream);
            String[] params = null;
            method.invoke(null, (Object) params);
            return Paths.get(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.setOut(null);
        }
        return null;
    }
}
