package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcesAtsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcesAtsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcesAtsSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcesInfomatFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcesInfomatFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcesInfomatSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcesMobileFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcesMobileFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcesMobileSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvSaveService;
import ru.cifrak.telecomit.backend.repository.*;

@Slf4j
@RestController
@RequestMapping("/api/import")
public class ApiImport {

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final RepositoryMobileType repositoryMobileType;

    private final LocationsSaveService locationsSaveService;

    private final TcesInternetSaveService tcesInternetSaveService;

    private final TcesMobileSaveService tcesMobileSaveService;

    private final TcesTvSaveService tcesTvSaveService;

    private final TcesRadioSaveService tcesRadioSaveService;

    private final TcesAtsSaveService tcesAtsSaveService;

    private final TcesPostSaveService tcesPostSaveService;

    private final TcesInfomatSaveService tcesInfomatSaveService;

    @Autowired
    public ApiImport(
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            RepositoryMobileType repositoryMobileType,
            LocationsSaveService locationsSaveService,
            TcesInternetSaveService tcesInternetSaveService,
            TcesMobileSaveService tcesMobileSaveService,
            TcesTvSaveService tcesTvSaveService,
            TcesRadioSaveService tcesRadioSaveService,
            TcesAtsSaveService tcesAtsSaveService,
            TcesPostSaveService tcesPostSaveService,
            TcesInfomatSaveService tcesInfomatSaveService) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.repositoryMobileType = repositoryMobileType;
        this.locationsSaveService = locationsSaveService;
        this.tcesInternetSaveService = tcesInternetSaveService;
        this.tcesMobileSaveService = tcesMobileSaveService;
        this.tcesTvSaveService = tcesTvSaveService;
        this.tcesRadioSaveService = tcesRadioSaveService;
        this.tcesAtsSaveService = tcesAtsSaveService;
        this.tcesPostSaveService = tcesPostSaveService;
        this.tcesInfomatSaveService = tcesInfomatSaveService;
    }

    @PostMapping("/location")
    public ResponseEntity<String> handleFileLocation(@RequestParam("file") MultipartFile file) {
        try {
            locationsSaveService.saveLocations(
                    new LocationsFromExcelDTOValidated(
                            repositoryLocation, new LocationsFromExcelDTO(file)
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
            tcesInternetSaveService.saveTcesInternet(
                    new TcesInternetFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            repositoryTypeTruncChannel,
                            new TcesInternetFromExcelDTO(file)
                    ).getTcesInternetDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-internet :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

    @PostMapping("/tc-mobile")
    public ResponseEntity<String> handleFileTcMobile(@RequestParam("file") MultipartFile file) {
        try {
            tcesMobileSaveService.saveTcesMobile(
                    new TcesMobileFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            repositoryMobileType,
                            new TcesMobileFromExcelDTO(file)
                    ).getTcesMobileDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-mobile :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-mobile :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

    @PostMapping("/tc-ats")
    public ResponseEntity<String> handleFileTcAts(@RequestParam("file") MultipartFile file) {
        try {
            tcesAtsSaveService.saveTces(
                    new TcesAtsFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            new TcesAtsFromExcelDTO(file)
                    ).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-ats :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-ats :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

    @PostMapping("/tc-tv")
    public ResponseEntity<String> handleFileTcTv(@RequestParam("file") MultipartFile file) {
        try {
            tcesTvSaveService.saveTces(
                    new TcesTvFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            new TcesTvFromExcelDTO(file)
                    ).getTcesTvDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-tv :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-tv :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

    @PostMapping("/tc-radio")
    public ResponseEntity<String> handleFileTcRadio(@RequestParam("file") MultipartFile file) {
        try {
            tcesRadioSaveService.saveTces(
                    new TcesRadioFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            new TcesRadioFromExcelDTO(file)
                    ).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-radio :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-radio :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

    @PostMapping("/tc-post")
    public ResponseEntity<String> handleFileTcPost(@RequestParam("file") MultipartFile file) {
        try {
            tcesPostSaveService.saveTces(
                    new TcesPostFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            new TcesPostFromExcelDTO(file)
                    ).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-internet :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
    }

//    @PostMapping("/tc-payphone")
//    public ResponseEntity<String> handleFileTcPayphone(@RequestParam("file") MultipartFile file) {
//        try {
//            tcesInternetSaveService.saveTcesInternet(
//                    new TcesInternetFromExcelDTOValidated(
//                            repositoryOperator,
//                            repositoryLocation,
//                            repositoryTypeTruncChannel,
//                            new TcesInternetFromExcelDTO(file)
//                    ).getTcesInternetDTO()
//            );
//        } catch (FromExcelDTOFormatException e) {
//            // TODO: <-, -> ?
//            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        log.info("POST /api/import/tc-internet :: {}", file.getOriginalFilename());
//        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
//    }

//    @PostMapping("/tc-infomat")
//    public ResponseEntity<String> handleFileTcInfomat(@RequestParam("file") MultipartFile file) {
//        // TODO: org.postgresql.util.PSQLException: ERROR: column writabletc0_.infomats does not exist
//        // column infomats not exist in technical_capability
//        try {
//            tcesInfomatSaveService.saveTces(
//                    new TcesInfomatFromExcelDTOValidated(
//                            repositoryOperator,
//                            repositoryLocation,
//                            new TcesInfomatFromExcelDTO(file)
//                    ).getTcesDTO()
//            );
//        } catch (FromExcelDTOFormatException e) {
//            // TODO: <-, -> ?
//            log.error("<-POST /api/import/tc-infomat :: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        log.info("POST /api/import/tc-infomat :: {}", file.getOriginalFilename());
//        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
//    }
//
//    @PostMapping("/tc-access-point")
//    public ResponseEntity<String> handleFileAccessPoint(@RequestParam("file") MultipartFile file) {
//        try {
//            tcesInternetSaveService.saveTcesInternet(
//                    new TcesInternetFromExcelDTOValidated(
//                            repositoryOperator,
//                            repositoryLocation,
//                            repositoryTypeTruncChannel,
//                            new TcesInternetFromExcelDTO(file)
//                    ).getTcesInternetDTO()
//            );
//        } catch (FromExcelDTOFormatException e) {
//            // TODO: <-, -> ?
//            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        log.info("POST /api/import/tc-internet :: {}", file.getOriginalFilename());
//        return ResponseEntity.ok(file.getOriginalFilename() + " was imported.");
//    }
}
