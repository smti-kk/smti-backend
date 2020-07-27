package ru.cifrak.telecomit.backend.base.station.controller;

import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.List;

@RequestMapping("/api/base-stations")
public interface BaseStationController {
    @GetMapping
    List<BaseStation> baseStations();

    @GetMapping("/{baseStation}")
    BaseStation baseStation(@PathVariable BaseStation baseStation) throws NotFoundException;

    @DeleteMapping("/{baseStationId}")
    void remove(@PathVariable Integer baseStationId);

    @PostMapping
    void save(@RequestBody BaseStation baseStation);
}
