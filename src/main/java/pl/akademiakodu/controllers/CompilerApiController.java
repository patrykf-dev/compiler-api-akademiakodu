package pl.akademiakodu.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.akademiakodu.services.CompilerApiService;

@CrossOrigin
@RestController
public class CompilerApiController {

    private CompilerApiService compilerApiService;

    @CrossOrigin
    @GetMapping("/api/v1/code/check")
    public String checkCode(@RequestParam String code,
                            @RequestParam String expectedResult) {
        String result = null;
        synchronized (this) {
            compilerApiService = new CompilerApiService(code,
                    expectedResult);
             result = compilerApiService.getResult();
        }
        return result;
    }

}
