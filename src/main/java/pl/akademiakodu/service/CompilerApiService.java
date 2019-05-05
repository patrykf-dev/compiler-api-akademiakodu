package pl.akademiakodu.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.akademiakodu.model.CodeValidator;

@Service
public class CompilerApiService {

    private CodeValidator codeValidator ;

    public CompilerApiService(){}

    public CompilerApiService(String code, String expectedResult){
        codeValidator = new CodeValidator(code,expectedResult);
    }

    @Async("taskExecutor")
    public String getResult(){
       return codeValidator.getResult();
    }

}
