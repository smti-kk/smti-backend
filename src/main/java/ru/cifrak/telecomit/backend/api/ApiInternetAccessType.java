package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;
import ru.cifrak.telecomit.backend.repository.RepositoryInternetAccessType;

import java.util.List;

@RestController
@RequestMapping("/api/type/internet-access")
public class ApiInternetAccessType {
    private RepositoryInternetAccessType repository;

    public ApiInternetAccessType(RepositoryInternetAccessType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeInternetAccess> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeInternetAccess one(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }
}
