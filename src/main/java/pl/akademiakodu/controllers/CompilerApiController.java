package pl.akademiakodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.JavaProject;
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
    public ResponseEntity<String> singleFileUpload(@RequestBody MultipartFile[] files) throws UnsupportedEncodingException {
        if (files.length == 0) {
            return new ResponseEntity<>("No files passed in request body", HttpStatus.NO_CONTENT);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return new ResponseEntity<>("The file " + file.getName() + " is empty!", HttpStatus.NO_CONTENT);
            }
        }

        Path path= null;
        try {
            for (MultipartFile file : files) {
                path = fileStorageService.saveUploadedFile(file.getBytes(), file.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // TODO: fix it, should pass the highest dir
        JavaProject javaProject = new JavaProject(path.getParent().getParent().getParent().toString());
        System.out.println(javaProject.toString());

        // @Async method
        // assuming packages like asd.asd.Main
        compilerApiService.validateProject(javaProject);
        return new ResponseEntity<>("Successfully uploaded file!", HttpStatus.OK);
    }

}
