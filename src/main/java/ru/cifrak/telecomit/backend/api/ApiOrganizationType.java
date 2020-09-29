package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeOrganization;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganizationType;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/type/organization")
public class ApiOrganizationType {
    private RepositoryOrganizationType repository;

    public ApiOrganizationType(RepositoryOrganizationType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeOrganization> list() {
        log.info("->GET /api/type/organization/");
        log.info("<- GET /api/type/organization/");
        return repository.findAll();
    }

}
