package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeSmo;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;
import ru.cifrak.telecomit.backend.repository.RepositorySmoType;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;

import java.util.List;

@RestController
@RequestMapping("/api/type/trunk-channel")
public class ApiTypeTrunkChannel {
    private RepositoryTypeTruncChannel repository;

    public ApiTypeTrunkChannel(RepositoryTypeTruncChannel repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public List<TypeTrunkChannel> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeTrunkChannel item(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

}
