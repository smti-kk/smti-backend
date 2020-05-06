/*
package ru.cifrak.telecomit.backend.api;

import org.locationtech.jts.geom.Polygon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.AccessPointSimple;
import ru.cifrak.telecomit.backend.api.enums.GovernmentProgram;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.utils.BboxFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accesspoints")
public class ApiAccessPoints {

    private RepositoryAccessPoints repository;

    public ApiAccessPoints(RepositoryAccessPoints repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/espd", params = "bbox")
    public List<AccessPointSimple> getAccessPointsEspd(@RequestParam("bbox") List<Double> bbox) {
        Polygon polygonBbox = new BboxFactory().createPolygon(bbox);

        return this.repository
                .getAccessPointsByGovernmentProgramShortNameAndBbox(GovernmentProgram.ESPD.getShortName(), polygonBbox)
                .stream()
                .map(AccessPointSimple::new)
                .collect(Collectors.toList());
    }


    @GetMapping(value = "/smo", params = "bbox")
    public List<AccessPointSimple> getAccessPointsSmo(@RequestParam("bbox") List<Double> bbox) {
        Polygon polygonBbox = new BboxFactory().createPolygon(bbox);

        return this.repository
                .getAccessPointsByGovernmentProgramShortNameAndBbox(GovernmentProgram.SMO.getShortName(), polygonBbox)
                .stream()
                .map(AccessPointSimple::new)
                .collect(Collectors.toList());
    }
}
*/
