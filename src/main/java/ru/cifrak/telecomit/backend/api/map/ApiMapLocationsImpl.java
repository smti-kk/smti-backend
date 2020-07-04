package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.repository.dto.ShortLocation;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsPositionRepository;
import ru.cifrak.telecomit.backend.repository.dto.MapLocation;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.util.List;

@RestController
public class ApiMapLocationsImpl implements ApiMapLocations {
    private final MapLocationsPositionRepository mapLocationsPositionRepository;
    private final BboxFactory bboxFactory;

    public ApiMapLocationsImpl(MapLocationsPositionRepository mapLocationsPositionRepository,
                               BboxFactory bboxFactory) {
        this.mapLocationsPositionRepository = mapLocationsPositionRepository;
        this.bboxFactory = bboxFactory;
    }

    @Override
    public List<MapLocation> list() {
        return this.mapLocationsPositionRepository.findAll();
    }

    @Override
    public List<MapLocation> listByBbox(List<Double> bbox) {
        return mapLocationsPositionRepository.findAllByBbox(
                bboxFactory.createPolygon(bbox)
        );
    }

    @Override
    public ShortLocation get(ShortLocation id) {
        if (id == null) {

        }
        return id;
    }
}
