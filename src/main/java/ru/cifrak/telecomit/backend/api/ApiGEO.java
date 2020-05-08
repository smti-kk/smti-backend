package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationAreaBorders;
import ru.cifrak.telecomit.backend.entities.GeoData;
import ru.cifrak.telecomit.backend.repository.RepositoryGeoLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class ApiGEO {
    private RepositoryLocation repository;
    private RepositoryGeoLocation repositoryGEO;

    public ApiGEO(RepositoryLocation repository, RepositoryGeoLocation repositoryGEO) {
        this.repository = repository;
        this.repositoryGEO = repositoryGEO;
    }

    /**
     * giving geo poligons for given area or for all
     *
     * @return Location DTO with id and name and POLIGON of border
     */
    @GetMapping("/location-area")
    public List<LocationAreaBorders> locations() {
        return repository.areaBorders().stream()
                .map(LocationAreaBorders::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/location-foo/")
    public List<GeoData> area() {
        List<GeoData> area = repositoryGEO.findAll();
        return area;
    }

}
