package pl.akademiakodu.compiling;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Path;


public interface CompilerExecutor {
    Path compileSource(Path javaFile);
    Path runClass(Path javaClass) throws MalformedURLException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException;
}
