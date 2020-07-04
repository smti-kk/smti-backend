package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;

import java.util.List;

@RequestMapping("/api/map-access-points")
public interface ApiMapAccessPoints {
    @GetMapping(params = "type")
    List<MapAccessPoint> list(@RequestParam("type") String type);

    @GetMapping(params = {"bbox", "type"})
    List<MapAccessPoint> list(@RequestParam("type") String type,
                              @RequestParam("bbox") List<Double> bbox);
}
