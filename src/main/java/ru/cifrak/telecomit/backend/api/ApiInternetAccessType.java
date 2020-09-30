package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeInternetAccess;
import ru.cifrak.telecomit.backend.repository.RepositoryInternetAccessType;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/type/internet-access")
public class ApiInternetAccessType {
    private RepositoryInternetAccessType repository;

    public ApiInternetAccessType(RepositoryInternetAccessType repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeInternetAccess> list() {
        log.info("->GET /api/type/internet-access/");
        log.info("<- GET /api/type/internet-access/");
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeInternetAccess one(@PathVariable Integer id) {
        log.info("->GET /api/type/internet-access::{}",id);
        log.info("<- GET /api/type/internet-access::{}",id);
        return repository.findById(id).orElse(null);
    }
}
