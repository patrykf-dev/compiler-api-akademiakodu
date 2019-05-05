package pl.akademiakodu.compiling;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSourceGenerator {

    Path generateJavaFileFromSourceCode(String codeToGenerate) throws IOException;

}
