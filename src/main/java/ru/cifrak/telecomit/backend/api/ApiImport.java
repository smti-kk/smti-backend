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
        try {
            this.saveLocations(new ParsingExcelLocations(file).getLocationsDTO());
        } catch (IOException e) {
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            return ResponseEntity.ok("{\"exception\":\"" + e.getMessage() + "\"}");
        }
        return ResponseEntity.ok("Locations are imported");
    }

    private void saveLocations(List<LocationFromExcelDTO> locationsDTO) {
        for (LocationFromExcelDTO locationDTO : locationsDTO){
            Location locationByFias = repository.findByFias(locationDTO.getFias());
            if (locationByFias != null) { ;
                locationByFias.setType(locationDTO.getType());
            } else {
//                @Id
//                @SequenceGenerator(name = "LOCATION_ID_GENERATOR", sequenceName = "location_id_seq", allocationSize = 1)
//                @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_ID_GENERATOR")
//                @Column(unique = true, nullable = false)
//                private Integer id;
//
//                @Column(nullable = false)
//                private Integer level;
//
//                @Column(nullable = false, length = 128)
//                private String name;
//
//                @Column(nullable = false)
//                private Integer population;
//
//                @Column(name = "type", nullable = false, length = 32)
//                private String type;
//
//                private UUID fias;
//                private String name;
//                private Integer population;
//                private String type;

                locationByFias = new Location();
                locationByFias.setFias(locationDTO.getFias());
                locationByFias.setName(locationDTO.getName());
                locationByFias.setPopulation(locationDTO.getPopulation());
                locationByFias.setType(locationDTO.getType());
            }
            repository.save(locationByFias);
        }
   }
}
