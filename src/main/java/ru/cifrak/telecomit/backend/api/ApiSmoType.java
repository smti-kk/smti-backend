package ru.cifrak.telecomit.backend.api;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganizationtype;
import ru.cifrak.telecomit.backend.domain.CatalogsSmotype;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization-smo-types")
public class ApiSmoType {
    private RepositorySmoType repository;

    public ApiSmoType(RepositorySmoType repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CatalogsSmotype> list(){
        return repository.findAll();
    }

}
