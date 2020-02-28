package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.domain.CatalogsSmotype;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class ApiLocation {
    private RepositoryLocation repository;

    public ApiLocation(RepositoryLocation repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CatalogsLocation> list(){
        return repository.findAll();
    }

    @GetMapping("/locations/")
    public List<CatalogsLocation> locations(){
        return repository.findAll();
    }
}
