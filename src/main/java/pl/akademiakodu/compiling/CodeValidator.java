package pl.akademiakodu.compiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.compiling.impl.SimpleCompilerExecutor;
import pl.akademiakodu.services.FileStorageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class CodeValidator {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private CompilerExecutor compilerExecutor;



    public String getResult(Path path, String expectedResult) {
        try {
            Path compiledFilePath = compilerExecutor.compileSource(path);
            Path output = compilerExecutor.runClass(compiledFilePath);
            StringBuilder stringResult = new StringBuilder();
            Stream<String> stream = Files.lines(output);
            stream.forEach(s -> stringResult.append(s));

            if (stringResult.toString().equals(expectedResult)) {
                return "Poprawna odpowiedź";
            } else {
                return "Błędna odpowiedź, oczekiwano wyniku: " + expectedResult
                        + " Otrzymano: " + stringResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
