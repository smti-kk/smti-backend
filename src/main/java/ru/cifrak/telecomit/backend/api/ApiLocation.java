package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationSimple;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.utils.BboxFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/location")
public class ApiLocation {
    private RepositoryLocation repository;

    public ApiLocation(RepositoryLocation repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LocationSimple> locations() {
        return repository.locations().stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping(params = "bbox")
    public List<LocationSimple> locationsByBbox(@RequestParam("bbox") List<Double> bbox) {
        return repository.locationsByBbox(new BboxFactory().createPolygon(bbox)).stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/parents/")
    public List<LocationSimple> parents() {
        return repository.parents().stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping(params = "parent")
    public List<LocationSimple> locationsByParent(@RequestParam("parent") Integer parentId) {
        return repository.findAllByParentId(parentId).stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }
}
