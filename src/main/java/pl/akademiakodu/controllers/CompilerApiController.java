package pl.akademiakodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.models.JavaProject;
import pl.akademiakodu.services.CompilerApiService;
import pl.akademiakodu.services.FileStorageService;

import java.io.IOException;
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
    public ResponseEntity<String> uploadProject(@RequestBody MultipartFile[] files) {
        ResponseEntity<String> response = verifyRequestBody(files);
        if (response != null) return response;

        // TODO: fix it, should pass the highest dir
        Path uploadedFile = saveUploadedFiles(files);
        JavaProject javaProject = new JavaProject(uploadedFile.getParent().getParent().getParent().toString());

        // @Async method
        compilerApiService.validateProject(javaProject);
        return new ResponseEntity<>("Successfully uploaded file!", HttpStatus.OK);
    }


    private Path saveUploadedFiles(MultipartFile[] files) {
        Path path = null;
        try {
            for (MultipartFile file : files) {
                path = fileStorageService.saveUploadedFile(file.getBytes(), file.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
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
