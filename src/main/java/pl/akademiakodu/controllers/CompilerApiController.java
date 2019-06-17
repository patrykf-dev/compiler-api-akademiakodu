package pl.akademiakodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.compiling.JavaProject;
import pl.akademiakodu.services.CompilerApiService;
import pl.akademiakodu.services.FileStorageService;

import java.io.IOException;

@CrossOrigin
@RestController
public class CompilerApiController {

    @Autowired
    private CompilerApiService compilerApiService;

    @Autowired
    private FileStorageService fileStorageService;

    private static int UPLOAD_ID = 1;

    @CrossOrigin
    @PostMapping("/code/upload")
    public ResponseEntity<String> uploadProject(@RequestBody MultipartFile[] files) {
        ResponseEntity<String> response = verifyRequestBody(files);
        if (response != null) return response;


        JavaProject javaProject = null;
        try {
            javaProject = fileStorageService.saveProject(UPLOAD_ID++, files);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Cannot upload files!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // @Async method
        compilerApiService.validateProject(javaProject);
        return new ResponseEntity<>("Successfully uploaded file!", HttpStatus.OK);
    }

    private ResponseEntity<String> verifyRequestBody(MultipartFile[] files) {
        if (files.length == 0) {
            return new ResponseEntity<>("No files passed in request body", HttpStatus.NO_CONTENT);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return new ResponseEntity<>("The file " + file.getName() + " is empty!", HttpStatus.NO_CONTENT);
            }
        }
        return null;
    }

}
