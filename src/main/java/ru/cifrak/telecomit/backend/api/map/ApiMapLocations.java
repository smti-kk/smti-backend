package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.api.dto.DtoMapLocation;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;
import ru.cifrak.telecomit.backend.entities.map.ShortLocation;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.map.MapLocationSearchResult;

import java.util.List;

@RequestMapping("/api/map-locations")
public interface ApiMapLocations {
    @GetMapping
    List<MapLocation> list();

    @GetMapping("/without-cellular")
    List<MapLocation> listWithoutCellular();

    @GetMapping("/with-cellular")
    List<MapLocation> listWithCellular();

    @GetMapping("/with-quality")
    List<DtoMapLocation> listWithQuality();

    @GetMapping(params = "bbox")
    List<MapLocation> listByBbox(@RequestParam("bbox") List<Double> bbox);

    @GetMapping("/{id}")
    ShortLocation get(@PathVariable Integer id) throws NotFoundException;

    @GetMapping(params = "search")
    List<MapLocationSearchResult> get(@RequestParam("search") String searchString);
}
