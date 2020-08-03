package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationFromExcelDTO;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import java.util.List;
import java.util.UUID;

@Service
public class TcesInternetSaveService {
    private final RepositoryWritableTc repository;

    public TcesInternetSaveService(RepositoryWritableTc repository) {
        this.repository = repository;
    }

    public void saveTces(List<LocationFromExcelDTO> locationsDTO) {
//        for (LocationFromExcelDTO locationDTO : locationsDTO){
//            Location locationByFias = repository.findByFias(UUID.fromString(locationDTO.getFias()));
//            Location parent = this.getLocationDTOParent(locationDTO);
//            int level = this.getLocationDTOLevel(locationDTO);
//            if (locationByFias != null) {
//                if (locationDTO.needUpdateName(locationByFias)) {
//                    locationByFias.setName(locationDTO.getName());
//                }
//                if (locationDTO.needUpdatePopulation(locationByFias)) {
//                    locationByFias.setPopulation(Integer.parseInt(locationDTO.getPopulation()));
//                }
//                if (locationDTO.needUpdateType(locationByFias)) {
//                    locationByFias.setType(locationDTO.getType());
//                }
//                if (locationByFias.getLevel() != 0 && locationByFias.getLevel() != level) {
//                    locationByFias.setLevel(level);
//                }
//                if (parent != null && !parent.equals(locationByFias.getParent())) {
//                    locationByFias.setParent(parent);
//                }
//            } else {
//                locationByFias = new Location();
//                locationByFias.setFias(UUID.fromString(locationDTO.getFias()));
//                locationByFias.setName(locationDTO.getName());
//                locationByFias.setPopulation(Integer.parseInt(locationDTO.getPopulation()));
//                locationByFias.setType(locationDTO.getType());
//                locationByFias.setLevel(level);
//                locationByFias.setParent(parent);
//            }
//            // TODO: Transaction.
//            repository.save(locationByFias);
//        }
    }

//    private Integer getLocationDTOLevel(LocationFromExcelDTO locationDTO) {
//        int level = repository.findMaxLevel();
//        Location mo = repository.findByNameAndType(locationDTO.getNameMO(), locationDTO.getTypeMO());
//        if (mo != null && mo.getLevel() < level) {
//            level = mo.getLevel() + 1;
//        }
//        return level;
//    }
//
//    private Location getLocationDTOParent(LocationFromExcelDTO locationDTO) {
//        return repository.findByNameAndType(locationDTO.getNameMO(), locationDTO.getTypeMO());
//    }
}
