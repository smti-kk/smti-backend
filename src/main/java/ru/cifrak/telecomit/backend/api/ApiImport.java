package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.LocationFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.ParsingExcelLocations;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/import")
public class ApiImport {

    private final RepositoryLocation repository;

    @Autowired
    public ApiImport(RepositoryLocation repository) {
        this.repository = repository;
    }

    @PostMapping("/location")
    public ResponseEntity<?> handleFile(@RequestParam("file") MultipartFile file) {
        List<LocationFromExcelDTO> locationsDTO;
        try {
            locationsDTO = new ParsingExcelLocations(file).getLocationsDTO();
        } catch (IOException e) {
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            return ResponseEntity.ok("{\"exception\":\"" + e.getMessage() + "\"}");
        }
        if (!this.checkTypesOfLocationsDTO(locationsDTO)) {
            return ResponseEntity.ok("{\"TypeError\":\"Types must be in: " + this.getListInOneRowOfTypesOfLocations() + "\"}");
        }
        this.saveLocations(locationsDTO);
        return ResponseEntity.ok("Locations are imported");
    }

    private String getListInOneRowOfTypesOfLocations() {
        StringJoiner result = new StringJoiner(", ");
        for (String type : repository.findAllTypes()) {
            result.add(type);
        }
        return result.toString();
    }

    private boolean checkTypesOfLocationsDTO(List<LocationFromExcelDTO> locationsDTO) {
        boolean result = true;
        List<String> typesOfLocationsDTO = repository.findAllTypes();
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!typesOfLocationsDTO.contains(locationDTO.getType()) || !typesOfLocationsDTO.contains(locationDTO.getTypeMO())) {
                result = false;
                break;
            }
        }
        return result;
    }

    private void saveLocations(List<LocationFromExcelDTO> locationsDTO) {
        for (LocationFromExcelDTO locationDTO : locationsDTO){
            Location locationByFias = repository.findByFias(locationDTO.getFias());
            int level = this.getLocationDTOLevel(locationDTO);
//            тер - Территория 5
//            п - Поселок сельского типа 4
//            с - Село 4
//            пгт - Поселок городского типа 4
//            гп - Городской посёлок ?
//            р-н - Район 2
//            г - Город 1, 3, 4
//            д - Деревня 5
//            край - Край 1
//            с/с - Сельсовет 3
            if (locationByFias != null) {
                if (!locationDTO.getName().trim().isEmpty() && !locationDTO.getName().equals(locationByFias.getName())) {
                    locationByFias.setName(locationDTO.getName());
                }
                if (locationDTO.getPopulation() != 0 && locationDTO.getPopulation() != locationByFias.getPopulation()) {
                    locationByFias.setPopulation(locationDTO.getPopulation());
                }
                if (!locationDTO.getType().trim().isEmpty() && !locationDTO.getType().equals(locationByFias.getType())) {
                    locationByFias.setType(locationDTO.getType());
                }
                if (locationByFias.getLevel() != 0 && level != locationByFias.getLevel()) {
                    locationByFias.setLevel(level);
                }
            } else {
                locationByFias = new Location();
                locationByFias.setFias(locationDTO.getFias());
                locationByFias.setName(locationDTO.getName());
                locationByFias.setPopulation(locationDTO.getPopulation());
                locationByFias.setType(locationDTO.getType());
                locationByFias.setLevel(2);
            }
            repository.save(locationByFias);
        }
    }

    private int getLocationDTOLevel(LocationFromExcelDTO locationDTO) {
        int level = repository.findMaxLevel();
        Location location = repository.findByNameAndType(locationDTO.getName(), locationDTO.getType());
        if (location != null && location.getLevel() <= level) {
            level = location.getLevel() + 1;
        }
        return level;
    }
}
