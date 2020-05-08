package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.repository.RepositoryGovernmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;

@RestController
@RequestMapping("/api/gov-program")
public class ApiGovPrograms {
    private RepositoryGovernmentProgram repository;

    public ApiGovPrograms(RepositoryGovernmentProgram repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<GovernmentDevelopmentProgram> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public GovernmentDevelopmentProgram item(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

}
