package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.domain.CatalogsGovernmentdevelopmentprogram;
import ru.cifrak.telecomit.backend.domain.CatalogsInternetaccesstype;
import ru.cifrak.telecomit.backend.repository.RepositoryGovernmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositoryInternetAccessType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gov-program")
public class ApiGovernmentProgram {
    private RepositoryGovernmentProgram repository;

    public ApiGovernmentProgram(RepositoryGovernmentProgram repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CatalogsGovernmentdevelopmentprogram> list(){
        return repository.findAll();
    }

}
