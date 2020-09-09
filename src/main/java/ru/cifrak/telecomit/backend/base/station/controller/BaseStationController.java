package ru.cifrak.telecomit.backend.base.station.controller;

import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.List;

@RequestMapping("/api/base-stations")
public interface BaseStationController {
    @GetMapping
    List<BaseStation> baseStations();

    @GetMapping("/{baseStationId}")
    BaseStation baseStation(@PathVariable Integer baseStationId) throws NotFoundException;

    @DeleteMapping("/{baseStationId}")
    void remove(@PathVariable Integer baseStationId);

    @PostMapping
    BaseStation save(@RequestBody BaseStation baseStation);

    @PutMapping
    BaseStation update(@RequestBody BaseStation baseStation);
}
