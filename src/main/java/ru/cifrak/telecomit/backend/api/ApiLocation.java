package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.LocationSimple;
import ru.cifrak.telecomit.backend.api.dto.LocationSimpleFilterDTO;
import ru.cifrak.telecomit.backend.entities.DLocationBase;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForReference;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.repository.RepositoryDLocationBase;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationForReference;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.annotation.Nullable;
import java.util.*;
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
            @RequestParam(value = "canBeParent", required = false) Boolean canBeParent,
            @RequestParam(value = "locations", required = false) List<Integer> locationIds,
            @RequestParam(value = "parents", required = false) List<Integer> parentIds,
            @RequestParam(value = "locationNames", required = false) List<String> locationNames,
            @RequestParam(value = "logicalCondition", required = false) LogicalCondition logicalCondition) {
        log.info("--> GET /api/location/location-reference/filtered/");
        Page<LocationForReference> list =
                repositoryLocationForReference.findAll(
                        locationService.getPredicateForLocationForReference(
                                canBeParent,
                                locationIds,
                                parentIds,
                                locationNames,
                                logicalCondition != null ? logicalCondition : LogicalCondition.AND),
                        pageable);
        log.info("<-- GET /api/location/location-reference/filtered/");
        return list;
    }


    @PostMapping("/location-reference/update/{id}/")
    @Transactional
    @Secured({"ROLE_ADMIN"})
    ResponseEntity<List<String>> updateLocation(
            @PathVariable(name = "id") final Integer locationId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "population", required = false) Integer population,
            @RequestParam(value = "parent", required = false) Integer parentId
    ) {
        log.info("--> GET /api/location/location-reference/update/{}/", locationId);
        ResponseEntity<List<String>> result;
        LocationForReference location = repositoryLocationForReference.findById(locationId).orElse(null);
        Location parent;
        if (parentId != null) {
            parent = repository.findById(parentId).orElse(null);
        } else {
            parent = null;
        }
        List<String> locationErrors = checkLocationForReference(
                locationId, parentId, location, type, population, parent);
        if (locationErrors.size() == 0) {
            boolean toUpdate = false;
            assert location != null;
            if (type != null && !type.equals(location.getType())) {
                location.setType(type);
                toUpdate = true;
            }
            if (population != null && !population.equals(location.getPopulation())) {
                location.setPopulation(population);
                toUpdate = true;
            }
            if (parentId != null) {
                assert parent != null;
                if (!parentId.equals(location.getLocationParent().getId())) {
                    location.setLocationParent(new LocationParent(parent));
                    toUpdate = true;
                }
            }
            if (toUpdate) {
                repositoryLocationForReference.save(location);
            }
            result = ResponseEntity.ok(locationErrors);
        } else {
            result = ResponseEntity.badRequest().body(locationErrors);
        }
        log.info("<-- GET /api/location/location-reference/update/{}/", locationId);
        return result;
    }

    private List<String> checkLocationForReference(Integer locationId,
                                                   Integer parentId,
                                                   @Nullable LocationForReference location,
                                                   String type,
                                                   Integer population,
                                                   @Nullable Location parent) {
        List<String> errors = new ArrayList<>();
        if (location == null) {
            errors.add("Not found specified location with id = " + locationId);
        } else if (LocationService.PARENT_LOCATION_TYPES.contains(location.getType())) {
            if (population != null || parentId != null) {
                errors.add("Location with type " + location.getType()
                        + " can be parent, then only type can be changed");
            }
            if (type != null) {
                if (LocationService.NOT_PARENT_LOCATION_TYPES.contains(type)) {
                    errors.add("Location with type " + location.getType()
                            + " can be parent, then can be changed only to parent type");
                } else if (!LocationService.PARENT_LOCATION_TYPES.contains(type)) {
                    errors.add("Location can no be updated to type " + type);
                }
            }
        } else if (LocationService.NOT_PARENT_LOCATION_TYPES.contains(location.getType())) {
            if (type != null) {
                if (LocationService.PARENT_LOCATION_TYPES.contains(type)) {
                    errors.add("Location with type " + location.getType()
                            + " cannot be parent, then can be changed only to not parent type");
                } else if (!LocationService.NOT_PARENT_LOCATION_TYPES.contains(type)) {
                    errors.add("Location can no be updated to type " + type);
                }
            }
            if (population != null && population < 0) {
                errors.add("Wrong population of location (" + population + " < 0)");
            } else if (parentId != null) {
                if (parent == null) {
                    errors.add("Not found parent location with id = " + parentId);
                } else {
                    if (parentId.equals(locationId)) {
                        errors.add("Location cannot be parent to itself");
                    }
                    if (!LocationService.PARENT_LOCATION_TYPES.contains(parent.getType())) {
                        errors.add("Type of specified parent location is " + parent.getType() + ", cannot be parent");
                    }
                }
            }
        } else {
            errors.add("Location with type " + location.getType() + " cannot be update");
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
