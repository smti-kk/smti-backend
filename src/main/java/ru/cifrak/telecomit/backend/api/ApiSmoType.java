package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;

@RestController
@RequestMapping("/api/type/smo")
public class ApiSmoType {
    private RepositorySmoType repository;

    public ApiSmoType(RepositorySmoType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeSmo> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeSmo item(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

}
