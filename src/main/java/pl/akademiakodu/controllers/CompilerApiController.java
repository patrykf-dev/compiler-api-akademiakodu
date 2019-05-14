package pl.akademiakodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.services.CompilerApiService;
import pl.akademiakodu.services.FileStorageService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

@CrossOrigin
@RestController
public class CompilerApiController {

    @Autowired
    private CompilerApiService compilerApiService;

    @Autowired
    private FileStorageService fileStorageService;


    @CrossOrigin
    @PostMapping("/code/upload")
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file) throws UnsupportedEncodingException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("The file is empty!", HttpStatus.NO_CONTENT);
        }

        Path path;
        try {
            path = fileStorageService.saveUploadedFile(file.getBytes(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



        // @Async method
        compilerApiService.validateFile(path);
        return new ResponseEntity<>("Successfully uploaded file!", HttpStatus.OK);
    }

}
