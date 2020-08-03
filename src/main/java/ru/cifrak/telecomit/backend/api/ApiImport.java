package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.location.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetSaveService;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

@Slf4j
@RestController
@RequestMapping("/api/import")
public class ApiImport {

    private final RepositoryLocation repository;

    private final LocationsSaveService locationsSaveService;

    private final TcesInternetSaveService tcesInternetSaveService;

    @Autowired
    public ApiImport(
            RepositoryLocation repository,
            LocationsSaveService locationsSaveService,
            TcesInternetSaveService tcesInternetSaveService) {
        this.repository = repository;
        this.locationsSaveService = locationsSaveService;
        this.tcesInternetSaveService = tcesInternetSaveService;
    }

    @PostMapping("/location")
    public ResponseEntity<String> handleFileLocation(@RequestParam("file") MultipartFile file) {
        try {
            locationsSaveService.saveLocations(
                    new LocationsFromExcelDTOValidated(
                            repository, new LocationsFromExcelDTO(file)
                    ).getLocationsDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/location :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

    @PostMapping("/tc-internet")
    public ResponseEntity<String> handleFileTcInternet(@RequestParam("file") MultipartFile file) {
        try {
            tcesInternetSaveService.saveTces(
                    new LocationsFromExcelDTOValidated(
                            repository, new LocationsFromExcelDTO(file)
                    ).getLocationsDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/location :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }
}
