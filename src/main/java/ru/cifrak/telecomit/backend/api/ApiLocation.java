package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.LocationFeaturesSaveRequest;
import ru.cifrak.telecomit.backend.api.dto.LocationSimple;
import ru.cifrak.telecomit.backend.api.dto.LocationSimpleFilterDTO;
import ru.cifrak.telecomit.backend.entities.DLocationBase;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.TypeLocation;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForReference;
import ru.cifrak.telecomit.backend.repository.RepositoryDLocationBase;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationForReference;
import ru.cifrak.telecomit.backend.service.LocationService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/location")
public class ApiLocation {
    private final RepositoryLocation repository;
    private final RepositoryDLocationBase rDLocationBase;
    private final RepositoryLocationForReference repositoryLocationForReference;
    private final LocationService locationService;

    public ApiLocation(RepositoryLocation repository,
                       RepositoryDLocationBase rDLocationBase,
                       RepositoryLocationForReference repositoryLocationForReference,
                       LocationService locationService) {
        this.repository = repository;
        this.rDLocationBase = rDLocationBase;
        this.repositoryLocationForReference = repositoryLocationForReference;
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationSimple> location(@RequestParam(value = "location") String name) {
        log.info("->GET /api/location/base/::{}",name);
        log.info("<- GET /api/location/base/::{}",name);
        return repository.locations(name).stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/locations/")
    public List<LocationSimpleFilterDTO> locations() {
        log.info("->GET /api/location/base/locations/");
        log.info("<- GET /api/location/base/locations/");
        return repository.locationFilter().stream()
                .map(LocationSimpleFilterDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/location-reference/filtered/")
    public Page<LocationForReference> getAllLocationReference(
            Pageable pageable,
            @RequestParam(value = "locations", required = false) List<Integer> locationIds,
            @RequestParam(value = "parents", required = false) List<Integer> parentIds,
            @RequestParam(value = "locationNames", required = false) List<String> locationNames,
            @RequestParam(value = "logicalCondition", required = false) LogicalCondition logicalCondition) {
        log.info("->GET /api/location/base/locations-reference/");
        log.info("<- GET /api/location/base/locations-reference/");
        Page<LocationForReference> list =
                repositoryLocationForReference.findAll(
                        locationService.getPredicateForLocationForReference(locationIds,
                                parentIds,
                                locationNames,
                                logicalCondition),
                        pageable);
        return list;
    }

    @PostMapping("/location-reference/update/")
    @Transactional
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    void updateLocation(@RequestBody LocationForReference location) {
        log.info("--> Update location for reference.");
        checkLocationForReference(location);
        repositoryLocationForReference.save(location);
        log.info("<-- Update location for reference.");
    }

    private void checkLocationForReference(LocationForReference location) {
        if (location.getPopulation() < 0) {
            throw new IllegalArgumentException("Population of location < 0");
        }
        if (Arrays.stream(TypeLocation.values())
                .filter(TypeLocation::isCanBeParent)
                .map(TypeLocation::getDescription)
                .collect(Collectors.toList())
                .contains(location.getType())) {
            throw new IllegalArgumentException("Wrong type of location");
        }
        if (Arrays.stream(TypeLocation.values())
                .filter(tl -> !tl.isCanBeParent())
                .map(TypeLocation::getDescription)
                .collect(Collectors.toList())
                .contains(location.getLocationParent().getType())) {
            throw new IllegalArgumentException("Wrong type of parent");
        }
    }

    @GetMapping("/parents/")
    @Cacheable("location_location_parents")
    public List<LocationSimple> parents() {
        log.info("->GET /api/location/base/parents/");
        log.info("<- GET /api/location/base/parents/");
        List<LocationSimple> orderedParents =
                repository.parents().stream()
                        .map(LocationSimple::new)
                        .collect(Collectors.toList());
        orderedParents.sort(((Comparator<LocationSimple>) (l1, l2) -> {
            int orderValueL1 =
                    l1.getType().equalsIgnoreCase("г") ? 1 :
                            l1.getType().equalsIgnoreCase("р-н") || l1.getType().equalsIgnoreCase("округ") ? 3 : 2;
            int orderValueL2 =
                    l2.getType().equalsIgnoreCase("г") ? 1 :
                            l2.getType().equalsIgnoreCase("р-н") || l2.getType().equalsIgnoreCase("округ") ? 3 : 2;
            return orderValueL1 - orderValueL2;
        }).thenComparing(LocationSimple::getName));
        return orderedParents;
    }

    @GetMapping(params = "parent")
    public List<LocationSimple> locationsByParent(@RequestParam("parent") Integer parentId) {
        log.info("->GET /api/location/base/::{}",parentId);
        log.info("<- GET /api/location/base/::{}",parentId);
        return repository.findAllByParentId(parentId).stream()
                .map(LocationSimple::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/base/")
    public List<DLocationBase> base() {
        log.info("->GET /api/location/base/");
        log.info("<- GET /api/location/base/");
        return rDLocationBase.findAllByTypeNotIn(Arrays.asList(new String[]{"с/с", "край", "тер"}));
    }
}
