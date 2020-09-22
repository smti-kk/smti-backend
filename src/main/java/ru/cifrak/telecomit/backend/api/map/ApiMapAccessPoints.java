package ru.cifrak.telecomit.backend.api.map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;

import java.util.Date;
import java.util.List;

@RequestMapping("/api/map-access-points")
public interface ApiMapAccessPoints {
    @GetMapping(params = "type")
    List<MapAccessPoint> list(@RequestParam("type") String type);

    @GetMapping(params = {"bbox", "type"})
    List<MapAccessPoint> list(@RequestParam("type") String type,
                              @RequestParam("bbox") List<Double> bbox);

    @GetMapping(params = {"modified", "type"})
    List<MapAccessPoint> list(@RequestParam("type") String type,
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("modified") Date modified);

    @GetMapping("/{accessPointId}/locationId")
    Integer locationId(@PathVariable Integer accessPointId);
}
