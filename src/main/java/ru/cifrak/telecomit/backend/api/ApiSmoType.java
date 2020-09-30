package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/type/smo")
public class ApiSmoType {
    private RepositorySmoType repository;

    public ApiSmoType(RepositorySmoType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeSmo> list() {
        log.info("->GET /api/type/smo/");
        log.info("<- GET /api/type/smo/");
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeSmo item(@PathVariable Integer id) {
        log.info("->GET /api/type/smo/::{}",id);
        log.info("<- GET /api/type/smo/::{}",id);
        return repository.findById(id).orElse(null);
    }

}
