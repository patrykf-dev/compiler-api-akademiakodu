package pl.akademiakodu.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.akademiakodu.compiling.CodeValidator;

import java.nio.charset.StandardCharsets;

@Service
public class CompilerApiService {

    public CompilerApiService() {
    }

    @Async
    public String validateFile(byte[] bytes) {
        String sourceCode = new String(bytes, StandardCharsets.UTF_8);
        CodeValidator codeValidator = new CodeValidator(sourceCode, "Hello world");
        return codeValidator.getResult();
    }

}
