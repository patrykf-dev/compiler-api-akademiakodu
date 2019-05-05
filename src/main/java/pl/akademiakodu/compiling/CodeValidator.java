package pl.akademiakodu.compiling;

import pl.akademiakodu.compiling.impl.SimpleCompilerExecutor;
import pl.akademiakodu.compiling.impl.SimpleFileSourceGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CodeValidator {

    private CompilerExecutor compilerExecutor = new SimpleCompilerExecutor();

    private FileSourceGenerator fileSourceGenerator = new
            SimpleFileSourceGenerator("D:\\Development\\compilerapi-results");

    private String code;
    private String expectedResult;

    public CodeValidator(String code, String expectedResult) {
        this.code = code;
        this.expectedResult = expectedResult;
    }

    public String getResult() {
        try {
            Path path = fileSourceGenerator.generateJavaFileFromSourceCode(code);
            Path filePath = compilerExecutor.compileSource(path);
            Path result = compilerExecutor.runClass(filePath);
            StringBuilder stringResult = new StringBuilder("");
            try (Stream<String> stream = Files.lines(result)) {
                stream.forEach(s -> stringResult.append(s.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (stringResult.toString().equals(expectedResult)) {
                return "Poprawna odpowiedź";
            } else {
                return "Błędna odpowiedź, oczekiwano wyniku: " + expectedResult
                        + " Otrzymano: " + stringResult;
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
