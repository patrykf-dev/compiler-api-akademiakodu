package pl.akademiakodu.compiling;

import pl.akademiakodu.JavaProject;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Path;


public interface CompilerExecutor {
    Path compileProject(JavaProject javaProject);
    Path runProject(JavaProject javaProject) throws Exception;
}
