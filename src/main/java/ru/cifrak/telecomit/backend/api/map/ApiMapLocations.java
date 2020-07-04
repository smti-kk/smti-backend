package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.repository.dto.MapLocation;
import ru.cifrak.telecomit.backend.repository.dto.ShortLocation;

import java.util.List;

@RequestMapping("/api/map-locations")
public interface ApiMapLocations {
    @GetMapping
    List<MapLocation> list();

    @GetMapping(params = "bbox")
    List<MapLocation> listByBbox(@RequestParam("bbox") List<Double> bbox);

    @GetMapping("/{id}")
    ShortLocation get(@PathVariable ShortLocation id);
}
