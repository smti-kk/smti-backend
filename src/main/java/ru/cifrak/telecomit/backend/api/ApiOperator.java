package ru.cifrak.telecomit.backend.api;

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

@RestController
@RequestMapping("/api/operator")
public class ApiOperator {
    private RepositoryOperator repository;

    public ApiOperator(RepositoryOperator repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Operator> item(@NotNull @PathVariable Integer id) {
        Optional<Operator> item = repository.findById(id);
        if (item.isPresent()){
            return ResponseEntity.ok(item.get());
        } else
            return ResponseEntity.notFound().build();
    }

    @GetMapping
    List<Operator> findAll() {
        return repository.findAll();
    }

    @GetMapping("/grouped")
    @Cacheable("grouped_operators")
    public Map<String, List<Operator>> grouped() {
        Map<String, List<Operator>> map = new HashMap<>();
        map.put("internet", repository.internet());
        map.put("mobile", repository.mobile());
        map.put("ats", repository.ats());
        map.put("radio", repository.radio());
        map.put("post", repository.postal());
        map.put("television", repository.television());
        map.put("payphone", repository.payphone());
        map.put("infomat", repository.infomat());
        return map;
    }

    @GetMapping("/internet")
    public List<Operator> internet() {
        return repository.internet();
    }

    @GetMapping("/mobile")
    public List<Operator> mobile() {
        return repository.mobile();
    }

    @GetMapping("/postal")
    public List<Operator> postal() {
        return repository.postal();
    }

    @GetMapping("/ats")
    public List<Operator> ats() {
        return repository.ats();
    }

    @GetMapping("/radio")
    public List<Operator> radio() {
        return repository.radio();
    }

    @GetMapping("/tv")
    public List<Operator> television() {
        return repository.television();
    }

}
