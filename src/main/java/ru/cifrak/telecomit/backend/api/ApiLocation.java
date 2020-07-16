package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationSimple;
import ru.cifrak.telecomit.backend.api.dto.LocationSimpleFilterDTO;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location")
public class ApiLocation {
    private RepositoryLocation repository;

    public ApiLocation(RepositoryLocation repository) {
        this.repository = repository;
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
