package ru.cifrak.telecomit.backend.api.map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.repository.map.MapAccessPointRepository;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ApiMapAccessPointsImpl implements ApiMapAccessPoints {
    private final MapAccessPointRepository mapAccessPointRepository;
    private final BboxFactory bboxFactory;

    public ApiMapAccessPointsImpl(MapAccessPointRepository mapAccessPointRepository, BboxFactory bboxFactory) {
        this.mapAccessPointRepository = mapAccessPointRepository;
        this.bboxFactory = bboxFactory;
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_ORGANIZATION"})
    public List<MapAccessPoint> list(String type) {
        return mapAccessPointRepository.findAll(type);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_ORGANIZATION"})
    public List<MapAccessPoint> list(String type, List<Double> bbox) {
        return mapAccessPointRepository.findAllByBbox(
                bboxFactory.createPolygon(bbox),
                type
        );
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_ORGANIZATION"})
    public List<MapAccessPoint> list(String type, Date modified) {
        return mapAccessPointRepository.findByModifiedAndType(type, modified);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_ORGANIZATION"})
    public Integer locationId(Integer accessPointId) {
        return mapAccessPointRepository.locationId(accessPointId);
    }

    @SendTo("/access-points/messages")
    @MessageMapping("/access-points/messages")
    public List<MapAccessPoint> send() {
        return new ArrayList<>();
    }
}
