package ru.cifrak.telecomit.backend.api.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPointDTO;
import ru.cifrak.telecomit.backend.repository.map.MapAccessPointRepository;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    public List<MapAccessPointDTO> list(TypeAccessPoint type) {
        List<MapAccessPointDTO> result = mapAccessPointRepository.findAll(type).stream().map(MapAccessPointDTO::new).collect(Collectors.toList());
        return result;
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_ORGANIZATION"})
    public List<MapAccessPointDTO> list(TypeAccessPoint type, List<Double> bbox) {
        List<MapAccessPointDTO> result = mapAccessPointRepository.findAllByBbox(
                bboxFactory.createPolygon(bbox),
                type
        ).stream().map(MapAccessPointDTO::new).collect(Collectors.toList());
        return result;
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_ORGANIZATION"})
    public List<MapAccessPointDTO> list(TypeAccessPoint type, LocalDateTime modified) {
//        log.info("maps period params: ::{}  ::{}", type, modified);
        return mapAccessPointRepository.findByModifiedAndType(type, modified.atZone(ZoneId.of("Asia/Krasnoyarsk")).toLocalDateTime())
                .stream()
                .map(MapAccessPointDTO::new)
                .collect(Collectors.toList());
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
