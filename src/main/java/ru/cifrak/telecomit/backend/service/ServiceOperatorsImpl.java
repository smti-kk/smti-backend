package ru.cifrak.telecomit.backend.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceOperatorsImpl implements ServiceOperators {
    private final RepositoryOperator repository;
    private final DBFileStorageService dbFileStorageService;

    public ServiceOperatorsImpl(RepositoryOperator repository, DBFileStorageService dbFileStorageService) {
        this.repository = repository;
        this.dbFileStorageService = dbFileStorageService;
    }

    public Operator item(Integer id) throws NotFoundException {
        Optional<Operator> item = repository.findById(id);
        return item.orElseThrow(NotFoundException::new);
    }

    public List<Operator> findAll() {
        return repository.findAll();
    }

    public Page<Operator> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable("grouped_operators")
    public Map<String, List<Operator>> grouped() {
        Map<String, List<Operator>> map = new HashMap<>();
        map.put("internet", internet());
        map.put("mobile", mobile());
        map.put("ats", ats());
        map.put("radio", radio());
        map.put("post", postal());
        map.put("television", television());
        map.put("payphone", payphone());
        map.put("infomat", infomat());
        return map;
    }

    public List<Operator> internet() {
        return repository.internet();
    }

    public List<Operator> mobile() {
        return repository.mobile();
    }

    public List<Operator> postal() {
        return repository.postal();
    }

    public List<Operator> ats() {
        return repository.ats();
    }

    public List<Operator> radio() {
        return repository.radio();
    }

    public List<Operator> television() {
        return repository.television();
    }

    @Override
    public List<Operator> payphone() {
        return repository.payphone();
    }

    @Override
    public List<Operator> infomat() {
        return repository.infomat();
    }

    @Override
    public Operator save(Operator operator) {
        return repository.save(operator);
    }

    @Override
    public String createIcon(MultipartFile icon) {
        return "/db-files/" + dbFileStorageService.storeFile(icon).getId();
    }

    public void delete(Integer id) {
        this.repository.deleteForce(id);
    }
}
