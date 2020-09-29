package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/type/trunk-channel")
public class ApiTypeTrunkChannel {
    private RepositoryTypeTruncChannel repository;

    public ApiTypeTrunkChannel(RepositoryTypeTruncChannel repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    @Cacheable("trunk_channels")
    public List<TypeTrunkChannel> list() {
        log.info("->GET " + "/api/type/trunk-channel/");
        log.info("<- GET " + "/api/type/trunk-channel/");
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public TypeTrunkChannel item(@PathVariable Integer id) {
        log.info("->GET " + "/api/type/trunk-channel/::{}",id);
        log.info("<- GET " + "/api/type/trunk-channel/::{}",id);
        return repository.findById(id).orElse(null);
    }

}
