package pl.akademiakodu.compiling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.akademiakodu.JavaProject;
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


    public String getResult(JavaProject javaProject, String expectedResult) {
        try {
            Path mainCompiledClassPath = null;

            synchronized (this) {
                mainCompiledClassPath = compilerExecutor.compileProject(javaProject);
            }

            Path output = compilerExecutor.runProject(javaProject);

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
