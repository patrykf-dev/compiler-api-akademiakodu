package pl.akademiakodu.model;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSourceGenerator {

    Path generateJavaFileFromSourceCode(String codeToGenerate) throws IOException;

}
