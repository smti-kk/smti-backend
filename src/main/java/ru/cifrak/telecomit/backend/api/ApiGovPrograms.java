package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositoryGovernmentProgram;
import ru.cifrak.telecomit.backend.service.LocationService;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/gov-program")
public class ApiGovPrograms {
    private final RepositoryGovernmentProgram repository;
    private final LocationService locationService;

    public ApiGovPrograms(RepositoryGovernmentProgram repository, LocationService locationService) {
        this.repository = repository;
        this.locationService = locationService;
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

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public GovernmentDevelopmentProgram createGovProgram(@RequestBody GovernmentDevelopmentProgram program) {
        locationService.refreshCache();
        return repository.save(program);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteGovProgram(@PathVariable Integer id) {
        locationService.refreshCache();
        repository.deleteById(id);
    }

}
