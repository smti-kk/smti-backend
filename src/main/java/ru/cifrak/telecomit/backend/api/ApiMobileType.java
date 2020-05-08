package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeMobile;
import ru.cifrak.telecomit.backend.repository.RepositoryMobileType;

import java.util.List;

@RestController
@RequestMapping("/api/type/mobile")
public class ApiMobileType {
    private RepositoryMobileType repository;

    public ApiMobileType(RepositoryMobileType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeMobile> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeMobile one(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

}
