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
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.repository.RepositoryDLocationBase;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

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

    public ApiLocation(RepositoryLocation repository, RepositoryDLocationBase rDLocationBase) {
        this.repository = repository;
        this.rDLocationBase = rDLocationBase;
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
