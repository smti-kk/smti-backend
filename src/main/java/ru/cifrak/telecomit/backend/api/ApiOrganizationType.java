package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeOrganization;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganizationType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization-types")
public class ApiOrganizationType {
    private RepositoryOrganizationType repository;

    public ApiOrganizationType(RepositoryOrganizationType repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TypeOrganization> list() {
        return repository.findAll();
    }

}
