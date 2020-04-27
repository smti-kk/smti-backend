package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.domain.CatalogsInternetaccesstype;
import ru.cifrak.telecomit.backend.domain.CatalogsMobiletype;
import ru.cifrak.telecomit.backend.domain.CatalogsSmotype;
import ru.cifrak.telecomit.backend.repository.RepositoryInternetAccessType;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internet-access-type")
public class ApiInternetAccessType {
    private RepositoryInternetAccessType repository;

    public ApiInternetAccessType(RepositoryInternetAccessType repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CatalogsInternetaccesstype> list(){
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public CatalogsInternetaccesstype one(@PathVariable Integer id){
        return repository.findById(id).orElse(null);
    }
}
