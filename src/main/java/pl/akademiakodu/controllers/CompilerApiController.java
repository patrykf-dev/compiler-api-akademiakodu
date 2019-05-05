package pl.akademiakodu.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.akademiakodu.services.CompilerApiService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
public class CompilerApiController {

    public static final String UPLOADED_FILES_STORAGE = "D:\\Development\\compiler-api-akademiakodu-results\\";
    private CompilerApiService compilerApiService;

    @CrossOrigin
    @PostMapping("/api/v1/code/upload")
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file) throws UnsupportedEncodingException {

        if (file.isEmpty()) {
            return new ResponseEntity<>("The file is empty!", HttpStatus.NO_CONTENT);
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FILES_STORAGE + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String result = "BLANK_RESULT";
        synchronized (this) {
            String sourceCode = new String(bytes, StandardCharsets.UTF_8);
            compilerApiService = new CompilerApiService(sourceCode, "asd");
            result = compilerApiService.getResult();
        }

        return new ResponseEntity<>("Successfully uploaded file " + result, HttpStatus.OK);
    }

}
