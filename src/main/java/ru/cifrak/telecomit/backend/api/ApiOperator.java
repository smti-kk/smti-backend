package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/operator")
public class ApiOperator {
    private RepositoryOperator repository;

    public ApiOperator(RepositoryOperator repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Operator> item(@NotNull @PathVariable Integer id) {
        log.info("->GET /api/operator/::{}",id);
        Optional<Operator> item = repository.findById(id);
        log.info("<- GET /api/operator/::{}",id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    List<Operator> findAll() {
        log.info("->GET /api/operator/");
        log.info("<- GET /api/operator/");
        return repository.findAll();
    }

    @GetMapping("/grouped")
    @Cacheable("grouped_operators")
    public Map<String, List<Operator>> grouped() {
        log.info("->GET /api/operator/grouped");
        Map<String, List<Operator>> map = new HashMap<>();
        map.put("internet", repository.internet());
        map.put("mobile", repository.mobile());
        map.put("ats", repository.ats());
        map.put("radio", repository.radio());
        map.put("post", repository.postal());
        map.put("television", repository.television());
        map.put("payphone", repository.payphone());
        map.put("infomat", repository.infomat());
        log.info("<- GET /api/operator/grouped");
        return map;
    }

    @GetMapping("/internet")
    public List<Operator> internet() {
        log.info("->GET /api/operator/internet");
        log.info("<- GET /api/operator/internet");
        return repository.internet();
    }

    @GetMapping("/mobile")
    public List<Operator> mobile() {
        log.info("->GET /api/operator/mobile");
        log.info("<- GET /api/operator/mobile");
        return repository.mobile();
    }

    @GetMapping("/postal")
    public List<Operator> postal() {
        log.info("->GET /api/operator/postal");
        log.info("<- GET /api/operator/postal");
        return repository.postal();
    }

    @GetMapping("/ats")
    public List<Operator> ats() {
        log.info("->GET /api/operator/ats");
        log.info("<- GET /api/operator/ats");
        return repository.ats();
    }

    @GetMapping("/radio")
    public List<Operator> radio() {
        log.info("->GET /api/operator/radio");
        log.info("<- GET /api/operator/radio");
        return repository.radio();
    }

    @GetMapping("/tv")
    public List<Operator> television() {
        log.info("->GET /api/operator/tv");
        log.info("<- GET /api/operator/tv");
        return repository.television();
    }

}
