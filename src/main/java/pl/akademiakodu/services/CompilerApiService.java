package pl.akademiakodu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.akademiakodu.compiling.ProjectValidationResult;
import pl.akademiakodu.compiling.ProjectValidator;
import pl.akademiakodu.models.UploadedProject;
import pl.akademiakodu.repositories.UploadedProjectRepository;

import java.io.IOException;

@Service
public class CompilerApiService {

    @Autowired
    private ProjectValidator projectValidator;

    @Autowired
    private UploadedProjectRepository uploadedProjectRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public CompilerApiService() {
    }

    @Async
    public void validateProject(UploadedProject uploadedProject) {
        String userOutput;
        String expectedOutput;
        ProjectValidationResult validationResult = null;
        try {
            validationResult = projectValidator.runProject(uploadedProject);

            if(validationResult == null) {
                userOutput = fileStorageService.getUserOutput(uploadedProject);
                expectedOutput = fileStorageService.getExpectedOutput(uploadedProject);
                validationResult = projectValidator.verifyOutput(userOutput, expectedOutput);
            }
        } catch (IOException e) {
            e.printStackTrace();
            validationResult = ProjectValidationResult.SERVER_ERROR;
        } finally {
            uploadedProject.setValidationResult(validationResult);
            uploadedProjectRepository.save(uploadedProject);
        }
    }
}
