package pl.akademiakodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akademiakodu.compiling.ProjectDetails;
import pl.akademiakodu.compiling.ProjectValidationResult;
import pl.akademiakodu.models.Task;
import pl.akademiakodu.models.UploadedProject;
import pl.akademiakodu.models.User;
import pl.akademiakodu.repositories.TaskRepository;
import pl.akademiakodu.repositories.UploadedProjectRepository;
import pl.akademiakodu.repositories.UserRepository;
import pl.akademiakodu.services.CompilerApiService;
import pl.akademiakodu.services.FileStorageService;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

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
        if(uploadedProject == null) {
            return new ResponseEntity<>("Invalid user or task!", HttpStatus.BAD_REQUEST);
        }
        uploadedProjectRepository.save(uploadedProject);

        try {
            ProjectDetails projectDetails = fileStorageService.saveProject(uploadedProject.getId(), files);
            uploadedProject.setProjectDetails(projectDetails);
            uploadedProject.setSourcePath(projectDetails.getSourcePath());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Cannot upload files!", HttpStatus.BAD_REQUEST);
        }

        compilerApiService.validateProject(uploadedProject); //runs in background
        return new ResponseEntity<>("Successfully uploaded project!", HttpStatus.OK);
    }

    private UploadedProject createProject(int userId, int taskId) {
        UploadedProject uploadedProject = new UploadedProject();

        Optional<User> u = userRepository.findById(userId);
        if(u.isPresent()) {
            uploadedProject.setUser(u.get());
        } else {
            return null;
        }

        Optional<Task> t = taskRepository.findById(taskId);
        if(t.isPresent()) {
            uploadedProject.setTask(t.get());
        } else {
            return null;
        }

        uploadedProject.setValidationResult(ProjectValidationResult.UPLOADED);

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
