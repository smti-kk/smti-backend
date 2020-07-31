package ru.cifrak.telecomit.backend.api.service;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.List;
import java.util.UUID;

@Service
public class LocationsSaveService {
    private final RepositoryLocation repository;

    public LocationsSaveService(RepositoryLocation repository) {
        this.repository = repository;
    }

    public void saveLocations(List<LocationFromExcelDTO> locationsDTO) {
        for (LocationFromExcelDTO locationDTO : locationsDTO){
            Location locationByFias = repository.findByFias(UUID.fromString(locationDTO.getFias()));
            int level = this.getLocationDTOLevel(locationDTO);
            if (locationByFias != null) {
                if (locationDTO.needUpdateName(locationByFias)) {
                    locationByFias.setName(locationDTO.getName());
                }
                if (locationDTO.needUpdatePopulation(locationByFias)) {
                    locationByFias.setPopulation(Integer.parseInt(locationDTO.getPopulation()));
                }
                if (locationDTO.needUpdateType(locationByFias)) {
                    locationByFias.setType(locationDTO.getType());
                }
                if (locationByFias.getLevel() != 0 && level != locationByFias.getLevel()) {
                    locationByFias.setLevel(level);
                }
            } else {
                locationByFias = new Location();
                locationByFias.setFias(UUID.fromString(locationDTO.getFias()));
                locationByFias.setName(locationDTO.getName());
                locationByFias.setPopulation(Integer.parseInt(locationDTO.getPopulation()));
                locationByFias.setType(locationDTO.getType());
                locationByFias.setLevel(level);
            }
            // TODO: Transaction.
            repository.save(locationByFias);
        }
    }

    private Integer getLocationDTOLevel(LocationFromExcelDTO locationDTO) {
        int level = repository.findMaxLevel();
        Location mo = repository.findByNameAndType(locationDTO.getNameMO(), locationDTO.getTypeMO());
        if (mo != null && mo.getLevel() < level) {
            level = mo.getLevel() + 1;
        }
        return level;
    }
}
