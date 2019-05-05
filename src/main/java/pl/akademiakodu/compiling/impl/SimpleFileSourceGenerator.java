package pl.akademiakodu.compiling.impl;

import pl.akademiakodu.compiling.FileSourceGenerator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class SimpleFileSourceGenerator implements FileSourceGenerator {

    private String rootPath;

    public SimpleFileSourceGenerator(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public Path generateJavaFileFromSourceCode(String codeToGenerate) throws IOException {
        String className = getClassName(codeToGenerate);
        List<String> lines = Arrays.asList(codeToGenerate);
        String filePath = rootPath + "/" + className + ".java";
        Path file = Paths.get(filePath);
        Files.write(file, lines, Charset.forName("UTF-8"));
        return file;
    }

    private String getClassName(String codeToGenerate) {
        int lengthOfClassWord = 5;
        int lastIndexOfClassWord = codeToGenerate.lastIndexOf("class") + lengthOfClassWord;
        int j = lastIndexOfClassWord;
        while (j < codeToGenerate.length()) {
            if (codeToGenerate.charAt(j) != '{') {
                j++;
            } else {
                break;
            }
        }
        String className = codeToGenerate.substring(lastIndexOfClassWord, j);
        return className.trim();
    }
}
