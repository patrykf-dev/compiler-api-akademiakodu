package pl.akademiakodu.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.akademiakodu.services.CompilerApiService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
public class CompilerApiController {

    public static final String UPLOADED_FILES_STORAGE = "D:\\Development\\compiler-api-akademiakodu-results\\";
    private CompilerApiService compilerApiService;

    @CrossOrigin
    @GetMapping("/api/v1/code/upload")
    public String checkCode(@RequestParam String code,
                            @RequestParam String expectedResult) {
        String result = null;
        synchronized (this) {
            compilerApiService = new CompilerApiService(code,
                    expectedResult);
             result = compilerApiService.getResult();
        }
        return result;
    }

    @PostMapping("/api/v1/code/upload/raw")
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return new ResponseEntity<>("The file is empty!", HttpStatus.NO_CONTENT);
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FILES_STORAGE + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not save file", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded file", HttpStatus.OK);
    }

}
