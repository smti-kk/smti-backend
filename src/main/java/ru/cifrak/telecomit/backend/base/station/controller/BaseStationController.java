package ru.cifrak.telecomit.backend.base.station.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.Date;
import java.util.List;

@RequestMapping("/api/base-stations")
public interface BaseStationController {
    @GetMapping(params = {"page", "size"})
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_CONTRACTOR"})
    Page<BaseStation> baseStations(
            Pageable pageable,
            @RequestParam(value = "operatorIds", required = false) List<Integer> operatorIds,
            @RequestParam(value = "typeMobiles", required = false) List<Integer> typeMobiles,
            @RequestParam(value = "coverageRadiusLeftBorder", required = false) Double coverageRadiusLeftBorder,
            @RequestParam(value = "coverageRadiusRightBorder", required = false) Double coverageRadiusRightBorder,
            @RequestParam(value = "propHeightLeftBorder", required = false) Double propHeightLeftBorder,
            @RequestParam(value = "propHeightRightBorder", required = false) Double propHeightRightBorder,
            @RequestParam(value = "actionDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date actionDateFrom,
            @RequestParam(value = "actionDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date actionDateTo,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(name = "logicalCondition", required = false) LogicalCondition logicalCondition
    );

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_CONTRACTOR"})
    List<BaseStation> baseStations();

    @GetMapping("/{baseStationId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_CONTRACTOR"})
    BaseStation baseStation(@PathVariable Integer baseStationId) throws NotFoundException;

    @DeleteMapping("/{baseStationId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    void remove(@PathVariable Integer baseStationId);

    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    BaseStation save(@RequestBody BaseStation baseStation);

    @PutMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    BaseStation update(@RequestBody BaseStation baseStation);
}
