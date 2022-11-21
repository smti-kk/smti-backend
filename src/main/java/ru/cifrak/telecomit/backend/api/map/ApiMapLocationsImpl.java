package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.DtoMapLocation;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;
import ru.cifrak.telecomit.backend.entities.map.ShortLocation;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.RepositoryTechnicalCapability;
import ru.cifrak.telecomit.backend.repository.map.MapLocationSearchResult;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsPositionRepository;
import ru.cifrak.telecomit.backend.repository.map.MapLocationsRepository;
import ru.cifrak.telecomit.backend.service.BboxFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiMapLocationsImpl implements ApiMapLocations {
    private final MapLocationsPositionRepository mapLocationsPositionRepository;
    private final MapLocationsRepository mapLocationsRepository;
    private final BboxFactory bboxFactory;
    private final RepositoryTechnicalCapability repositoryTechnicalCapability;

    public ApiMapLocationsImpl(MapLocationsPositionRepository mapLocationsPositionRepository,
                               MapLocationsRepository mapLocationsRepository,
                               BboxFactory bboxFactory,
                               RepositoryTechnicalCapability repositoryTechnicalCapability) {
        this.mapLocationsPositionRepository = mapLocationsPositionRepository;
        this.mapLocationsRepository = mapLocationsRepository;
        this.bboxFactory = bboxFactory;
        this.repositoryTechnicalCapability = repositoryTechnicalCapability;
    }

    @Override
    public List<MapLocation> list() {
        return this.mapLocationsPositionRepository.findAll();
    }

    @Override
    public List<MapLocation> listWithoutCellular() {
        return this.mapLocationsPositionRepository.findAllWithoutCellular();
    }

    @Override
    public List<MapLocation> listWithCellular() {
        return this.mapLocationsPositionRepository.findAllWithCellular();
    }

    @Override
    public List<DtoMapLocation> listWithQuality() {
        return mapLocationsPositionRepository.findAll()
                .stream()
                .filter(s -> s.getGeoData() != null && !s.getType().equals("край") && !s.getType().equals("го")
                && !s.getType().equals("мо") && !s.getType().equals("р-н") &&
                        repositoryTechnicalCapability.findById(Long.valueOf(s.getId())).isPresent())
                .map(s -> new DtoMapLocation(s, repositoryTechnicalCapability.findById(Long.valueOf(s.getId())).get()
                        .getQuality().name()))
                .collect(Collectors.toList());
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
