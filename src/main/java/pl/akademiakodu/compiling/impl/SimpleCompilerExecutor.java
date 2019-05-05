package pl.akademiakodu.compiling.impl;

import org.springframework.stereotype.Component;
import pl.akademiakodu.compiling.CompilerExecutor;
import pl.akademiakodu.controllers.CompilerApiController;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SimpleCompilerExecutor implements CompilerExecutor {

    public Path compileSource(Path javaFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());
        String path = javaFile.getParent().resolve(javaFile + ".class").toString().replace(".java", "");
        return Paths.get(path);
    }

    public Path runClass(Path javaClass)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        URL classUrl = javaClass.getParent().toFile().toURI().toURL();
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
        Class<?> clazz = Class.forName("Hello", true, classLoader);
        clazz.newInstance();
        Method meth = clazz.getMethod("main", String[].class);
        try {
            String fileName = CompilerApiController.UPLOADED_FILES_STORAGE + "output\\result.txt";
            PrintStream fileStream = new PrintStream(fileName);
            System.setOut(fileStream);
            String[] params = null; // init params accordingly
            meth.invoke(null, (Object) params);
            return Paths.get(fileName);
        } catch (Exception e) {
            System.out.println("BŁĄD" + e.getMessage());

        } finally {
            System.setOut(null);
        }
        return null;
    }
}
