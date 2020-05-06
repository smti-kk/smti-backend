package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
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
    public List<TypeSmo> list() {
        return repository.findAll();
    }

}
