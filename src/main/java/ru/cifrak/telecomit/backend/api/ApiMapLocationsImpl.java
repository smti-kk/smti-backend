package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.repository.MapLocationsRepository;
import ru.cifrak.telecomit.backend.repository.dto.MapLocation;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.util.List;

@RestController
public class ApiMapLocationsImpl implements ApiMapLocations {
    private final MapLocationsRepository mapLocationsRepository;
    private final BboxFactory bboxFactory;

    public ApiMapLocationsImpl(MapLocationsRepository mapLocationsRepository,
                               BboxFactory bboxFactory) {
        this.mapLocationsRepository = mapLocationsRepository;
        this.bboxFactory = bboxFactory;
    }

    @Override
    public List<MapLocation> list() {
        return this.mapLocationsRepository.findAll();
    }

    @Override
    public List<MapLocation> listByBbox(List<Double> bbox) {
        return mapLocationsRepository.findAllByBbox(
                bboxFactory.createPolygon(bbox)
        );
    }
}
