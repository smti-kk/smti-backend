package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositoryGovernmentProgram;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gov-program")
public class ApiGovernmentProgram {
    private RepositoryGovernmentProgram repository;

    public ApiGovernmentProgram(RepositoryGovernmentProgram repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<GovernmentDevelopmentProgram> list() {
        return repository.findAll();
    }

}
