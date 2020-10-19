package ru.cifrak.telecomit.backend.api.map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPointDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequestMapping("/api/map-access-points")
public interface ApiMapAccessPoints {
    @GetMapping(params = "type")
    List<MapAccessPointDTO> list(@RequestParam("type") TypeAccessPoint type);

    @GetMapping(params = {"bbox", "type"})
    List<MapAccessPointDTO> list(@RequestParam("type") TypeAccessPoint type,
                              @RequestParam("bbox") List<Double> bbox);

    @GetMapping(params = {"modified", "type"})
    List<MapAccessPointDTO> list(@RequestParam("type") TypeAccessPoint type,
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("modified") LocalDateTime modified);

    @GetMapping("/{accessPointId}/locationId")
    Integer locationId(@PathVariable Integer accessPointId);
}
