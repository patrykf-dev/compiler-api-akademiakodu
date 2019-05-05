package pl.akademiakodu.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class SimpleFileGenerator implements FileSourceGenerator {

    private String rootPath;

    public SimpleFileGenerator(String rootPath) {
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

    public static void main(String[] args) {
        SimpleFileGenerator simpleFileGenerator = new SimpleFileGenerator("D:\\Development\\compilerapi-results");
        try {
            Path path = simpleFileGenerator.generateJavaFileFromSourceCode("" +
                    "public class Hello{\n" +
                    "        public static void main(String[] args){\n" +
                    "System.out.println(\"Hello world\");" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "      }\n" +
                    "    }");

            CompilerSimpleExecutor compilerExecutor = new CompilerSimpleExecutor();
            Path compiledClass = compilerExecutor.compileSource(path);
            Path p = compilerExecutor.compileSource(compiledClass);
            compilerExecutor.runClass(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
