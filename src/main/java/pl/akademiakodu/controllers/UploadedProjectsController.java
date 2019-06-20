package pl.akademiakodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.compiling.ProjectDetails;
import pl.akademiakodu.models.UploadedProject;
import pl.akademiakodu.repositories.TaskRepository;
import pl.akademiakodu.repositories.UploadedProjectRepository;
import pl.akademiakodu.repositories.UserRepository;
import pl.akademiakodu.services.CompilerApiService;
import pl.akademiakodu.services.FileStorageService;

import java.io.IOException;
import java.sql.Date;

@CrossOrigin
@RestController
public class UploadedProjectsController {

    @Autowired
    private CompilerApiService compilerApiService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UploadedProjectRepository uploadedProjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @CrossOrigin
    @PostMapping("/projects/upload")
    public ResponseEntity<String> uploadProject(@RequestBody MultipartFile[] files, @RequestParam int userId, @RequestParam int taskId) {
        ResponseEntity<String> response = verifyRequestBody(files);
        if (response != null) return response;


        UploadedProject uploadedProject = createProject(userId, taskId);
        uploadedProjectRepository.save(uploadedProject);

        try {
            ProjectDetails projectDetails = fileStorageService.saveProject(uploadedProject.getId(), files);
            uploadedProject.setProjectDetails(projectDetails);
            uploadedProject.setSourcePath(projectDetails.getSourcePath());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Cannot upload files!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        compilerApiService.validateProject(uploadedProject); //runs in background
        return new ResponseEntity<>("Successfully uploaded file!", HttpStatus.OK);
    }

    private UploadedProject createProject(int userId, int taskId) {
        UploadedProject uploadedProject = new UploadedProject();
        if (userRepository.findById(userId).isPresent())
            uploadedProject.setUser(userRepository.findById(userId).get());
        if (taskRepository.findById(taskId).isPresent())
            uploadedProject.setTask(taskRepository.findById(taskId).get());
        uploadedProject.setUploadDate(new Date(System.currentTimeMillis()));
        return uploadedProject;
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
