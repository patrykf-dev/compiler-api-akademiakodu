package pl.akademiakodu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.akademiakodu.compiling.ProjectValidationResult;
import pl.akademiakodu.compiling.ProjectDetails;
import pl.akademiakodu.compiling.ProjectValidator;
import pl.akademiakodu.models.UploadedProject;
import pl.akademiakodu.repositories.UploadedProjectRepository;

@Service
public class CompilerApiService {

    @Autowired
    private ProjectValidator projectValidator;

    @Autowired
    private UploadedProjectRepository uploadedProjectRepository;

    public CompilerApiService() {
    }

    @Async
    public void validateProject(UploadedProject uploadedProject) {
        ProjectValidationResult rc = projectValidator.validateProject(uploadedProject, "Hello world");
        uploadedProject.setValidationResult(rc);
        uploadedProjectRepository.save(uploadedProject);
    }

}
