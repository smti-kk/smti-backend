package ru.cifrak.telecomit.backend.api.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.LocationProvidingInfo;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;

import java.util.List;

@RequestMapping("/api/detail-locations")
public interface ApiLocationDetail {

    @GetMapping()
    Page<LocationForTable> getList(Pageable pageable,
                                   @RequestParam(name = "mobile-type", required = false) List<Integer> typeMobiles,
                                   @RequestParam(name = "internet-type", required = false) List<Integer> internetTypes,
                                   @RequestParam(name = "internet-operators", required = false) List<Integer> internetOperators,
                                   @RequestParam(name = "cellular-operators", required = false) List<Integer> cellularOperators,
                                   @RequestParam(name = "is-logical-or", defaultValue = "true") Boolean isLogicalOr,
                                   @RequestParam(name = "location", required = false) String location,
                                   @RequestParam(name = "parent", required = false) String parent
    );

    @GetMapping("/{id}")
    LocationForTable getOne(@PathVariable Integer id);

    @GetMapping("/parents")
    List<LocationParent> parents();

    @GetMapping("/gov-years")
    List<Integer> govProgramYears();

    @GetMapping("/location-providing-info/{locationId}")
    LocationProvidingInfo locationProvidingInfo(@PathVariable Integer locationId);

    @PostMapping("/export-excel")
    void exportExcel(@RequestBody List<Integer> locationIds);
}