package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.entities.map.ShortLocation;
import ru.cifrak.telecomit.backend.repository.map.MapLocationSearchResult;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsPositionRepository;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsRepository;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.util.List;

@RestController
public class ApiMapLocationsImpl implements ApiMapLocations {
    private final MapLocationsPositionRepository mapLocationsPositionRepository;
    private final MapLocationsRepository mapLocationsRepository;
    private final BboxFactory bboxFactory;

    public ApiMapLocationsImpl(MapLocationsPositionRepository mapLocationsPositionRepository,
                               MapLocationsRepository mapLocationsRepository,
                               BboxFactory bboxFactory) {
        this.mapLocationsPositionRepository = mapLocationsPositionRepository;
        this.mapLocationsRepository = mapLocationsRepository;
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
    public ShortLocation get(Integer id) throws NotFoundException {
        ShortLocation location = mapLocationsRepository.get(id);
        if (location == null) {
            throw new NotFoundException();
        }
        return location;
    }

    @Override
    public List<MapLocationSearchResult> get(String searchString) {
        return mapLocationsRepository.findByName(searchString);
    }
}
