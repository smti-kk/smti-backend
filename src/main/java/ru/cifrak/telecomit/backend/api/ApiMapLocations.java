package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.repository.dto.MapLocation;

import java.util.List;

@RequestMapping("/api/map-locations")
public interface ApiMapLocations {
    @GetMapping
    List<MapLocation> list();

    @GetMapping(params = "bbox")
    List<MapLocation> listByBbox(@RequestParam("bbox") List<Double> bbox);
}
