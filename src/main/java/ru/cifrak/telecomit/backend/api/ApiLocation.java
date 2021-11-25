package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.repository.RepositoryDLocationBase;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationForReference;
import ru.cifrak.telecomit.backend.service.LocationService;

import java.util.ArrayList;
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
        log.info("--> GET /api/location/location-reference/filtered/");
        Page<LocationForReference> list =
                repositoryLocationForReference.findAll(
                        locationService.getPredicateForLocationForReference(locationIds,
                                parentIds,
                                locationNames,
                                logicalCondition),
                        pageable);
        log.info("<-- GET /api/location/location-reference/filtered/");
        return list;
    }

    @PostMapping("/location-reference/update/{id}/")
    @Transactional
    @Secured({"ROLE_ADMIN"})
    ResponseEntity<List<String>> updateLocation(
            @PathVariable(name = "id") final LocationForReference location,
            @RequestParam(value = "type", required = false) TypeLocation type,
            @RequestParam(value = "population", required = false) Integer population,
            @RequestParam(value = "parent", required = false) LocationParent parent
    ) {
        ResponseEntity<List<String>> result;
        log.info("--> GET /api/location/location-reference/update/{}/", location.getId());
        List<String> locationErrors = checkLocationForReference(type, population, parent);
        if (locationErrors.size() == 0) {
            location.setType(type.toString());
            location.setPopulation(population);
            location.setLocationParent(parent);
            repositoryLocationForReference.save(location);
            result = ResponseEntity.ok(locationErrors);
        } else {
            result = ResponseEntity.badRequest().body(locationErrors);
        }
        log.info("<-- GET /api/location/location-reference/update/{}/", location.getId());
        return result;
    }

    private List<String> checkLocationForReference(TypeLocation type, Integer population, LocationParent parent) {
        List<String> errors = new ArrayList<>();
        if (type != null && Arrays.stream(TypeLocation.values())
                .filter(TypeLocation::isCanBeParent)
                .map(TypeLocation::getDescription)
                .collect(Collectors.toList())
                .contains(type.toString())) {
            errors.add("Wrong type location");
        } else if (population < 0) {
            errors.add("Wrong population of location");
        } else if (!Arrays.stream(TypeLocation.values())
                .filter(tl -> !tl.isCanBeParent())
                .map(TypeLocation::getDescription)
                .collect(Collectors.toList())
                .contains(parent.getType())) {
            errors.add("Wrong type of location parent");
        }
        return errors;
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
