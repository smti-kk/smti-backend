package ru.cifrak.telecomit.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganizationtype;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;
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
    public List<CatalogsOrganizationtype> list(){
        return repository.findAll();
    }

}
