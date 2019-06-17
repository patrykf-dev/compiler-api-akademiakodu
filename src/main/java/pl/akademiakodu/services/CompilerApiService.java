package pl.akademiakodu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.akademiakodu.compiling.ProjectValidationResult;
import pl.akademiakodu.compiling.JavaProject;
import pl.akademiakodu.compiling.ProjectValidator;

@Service
public class CompilerApiService {

    @Autowired
    private ProjectValidator projectValidator;

    public CompilerApiService() { }

    @Async
    public ProjectValidationResult validateProject(JavaProject javaProject) {
        return projectValidator.validateProject(javaProject, "Hello world");
    }

}
