package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationSimple;
import ru.cifrak.telecomit.backend.api.dto.LocationSimpleFilterDTO;
import ru.cifrak.telecomit.backend.entities.DLocationBase;
import ru.cifrak.telecomit.backend.repository.RepositoryDLocationBase;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/location")
public class ApiLocation {
    private final RepositoryLocation repository;
    private final RepositoryDLocationBase rDLocationBase;

    public ApiLocation(RepositoryLocation repository, RepositoryDLocationBase rDLocationBase) {
        this.repository = repository;
        this.rDLocationBase = rDLocationBase;
    }

    @GetMapping
    public List<LocationSimple> location(@RequestParam(value = "location") String name) {
        return repository.locations(name).stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/locations/")
    public List<LocationSimpleFilterDTO> locations() {
        return repository.locationFilter().stream()
                .map(LocationSimpleFilterDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/parents/")
    @Cacheable("location_location_parents")
    public List<LocationSimple> parents() {

        return repository.parents().stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping(params = "parent")
    public List<LocationSimple> locationsByParent(@RequestParam("parent") Integer parentId) {
        log.info("->GET /api/operator/base/");
        log.info("<- GET /api/operator/base/");
        return repository.findAllByParentId(parentId).stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/base/")
    public List<DLocationBase> base() {
        log.info("->GET /api/operator/base/");
        log.info("<- GET /api/operator/base/");
        return rDLocationBase.findAll();
    }


}
