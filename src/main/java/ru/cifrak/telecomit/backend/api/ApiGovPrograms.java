package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.repository.RepositoryGovernmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/gov-program")
public class ApiGovPrograms {
    private RepositoryGovernmentProgram repository;

    public ApiGovPrograms(RepositoryGovernmentProgram repository) {
        this.repository = repository;
    }

    @GetMapping
    @Cacheable("gov_programs")
    public List<GovernmentDevelopmentProgram> list() {
        log.info("->GET /api/gov-program");
        log.info("<- GET /api/gov-program");
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public GovernmentDevelopmentProgram item(@PathVariable Integer id) {
        log.info("->GET /api/gov-program/::{}",id);
        log.info("<- GET /api/gov-program/::{}",id);
        return repository.findById(id).orElse(null);
    }

}
