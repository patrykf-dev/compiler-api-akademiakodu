package pl.akademiakodu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.akademiakodu.compiling.CodeValidator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class CompilerApiService {

    @Autowired
    private CodeValidator codeValidator;

    public CompilerApiService() { }

    @Async
    public String validateFile(Path path) {
        return codeValidator.getResult(path, "Hello world");
    }

}
