package ru.cifrak.telecomit.backend.api.map;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.DtoMapLocation;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;
import ru.cifrak.telecomit.backend.entities.map.ShortLocation;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
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
    private final RepositoryLocation repositoryLocation;

    public ApiMapLocationsImpl(MapLocationsPositionRepository mapLocationsPositionRepository,
                               MapLocationsRepository mapLocationsRepository,
                               BboxFactory bboxFactory,
                               RepositoryLocation repositoryLocation) {
        this.mapLocationsPositionRepository = mapLocationsPositionRepository;
        this.mapLocationsRepository = mapLocationsRepository;
        this.bboxFactory = bboxFactory;
        this.repositoryLocation = repositoryLocation;
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
                    .map(s -> new DtoMapLocation(s, repositoryLocation.findById(s.getId()).get()
                            .getTechnicalCapabilities().stream()
                            .filter(tc -> tc.getDecriminatorValue().equals("MOBILE"))
                            .map(t -> t.getQuality().name())
                            .collect(Collectors.toList())))
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
