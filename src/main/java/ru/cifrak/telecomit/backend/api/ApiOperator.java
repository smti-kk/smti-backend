package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/operator")
public class ApiOperator {
    private RepositoryOperator repository;

    public ApiOperator(RepositoryOperator repository) {
        this.repository = repository;
    }

    @GetMapping("/grouped")
    public Map<String, List<Operator>> grouped() {
        Map<String, List<Operator>> map = new HashMap<>();
        map.put("internet",repository.internet());
        map.put("mobile", repository.mobile());
        map.put("ats", repository.ats());
        map.put("radio", repository.radio());
        map.put("post", repository.postal());
        map.put("television", repository.television());
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
