package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApesFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApesFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApesSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationsSaveService;
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
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcesPayphoneFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcesPayphoneFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcesPayphoneSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.ImportResultTrunkChannel;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelsSaveService;
import ru.cifrak.telecomit.backend.repository.*;

// TODO: добавить метод handleFileTcpPayphone (?)

@Slf4j
@RestController
@RequestMapping("/api/import")
public class ApiImport {
    private final RepositoryLocation repositoryLocation;
    private final RepositoryOperator repositoryOperator;
    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;
    private final RepositoryMobileType repositoryMobileType;
    private final RepositoryOrganization repositoryOrganization;
    private final RepositoryInternetAccessType repositoryInternetAccessType;
    private final RepositorySmoType repositorySmoType;
    private final RepositoryOrganizationType repositoryOrganizationType;
    private final LocationsSaveService locationsSaveService;
    private final TcesInternetSaveService tcesInternetSaveService;
    private final TcesMobileSaveService tcesMobileSaveService;
    private final TcesTvSaveService tcesTvSaveService;
    private final TcesRadioSaveService tcesRadioSaveService;
    private final TcesPayphoneSaveService tcesPayphoneSaveService;
    private final TcesPostSaveService tcesPostSaveService;
    private final TcesInfomatSaveService tcesInfomatSaveService;
    private final ApesSaveService apesSaveService;
    private final TcesAtsSaveService tcesAtsSaveService;
    private final TrunkChannelsSaveService trunkChannelsSaveService;
    private final BaseStationsSaveService baseStationsSaveService;
    private final RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram;

    public ApiImport(
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            RepositoryMobileType repositoryMobileType,
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType,
            LocationsSaveService locationsSaveService,
            TcesInternetSaveService tcesInternetSaveService,
            TcesMobileSaveService tcesMobileSaveService,
            TcesTvSaveService tcesTvSaveService,
            TcesRadioSaveService tcesRadioSaveService,
            TcesPayphoneSaveService tcesPayphoneSaveService,
            TcesPostSaveService tcesPostSaveService,
            TcesInfomatSaveService tcesInfomatSaveService,
            ApesSaveService apesSaveService,
            TcesAtsSaveService tcesAtsSaveService,
            TrunkChannelsSaveService trunkChannelsSaveService,
            BaseStationsSaveService baseStationsSaveService,
            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.repositoryMobileType = repositoryMobileType;
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
        this.locationsSaveService = locationsSaveService;
        this.tcesInternetSaveService = tcesInternetSaveService;
        this.tcesMobileSaveService = tcesMobileSaveService;
        this.tcesTvSaveService = tcesTvSaveService;
        this.tcesRadioSaveService = tcesRadioSaveService;
        this.tcesPayphoneSaveService = tcesPayphoneSaveService;
        this.tcesPostSaveService = tcesPostSaveService;
        this.tcesInfomatSaveService = tcesInfomatSaveService;
        this.apesSaveService = apesSaveService;
        this.tcesAtsSaveService = tcesAtsSaveService;
        this.trunkChannelsSaveService = trunkChannelsSaveService;
        this.baseStationsSaveService = baseStationsSaveService;
        this.repositoryGovernmentDevelopmentProgram = repositoryGovernmentDevelopmentProgram;
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-payphone")
    public ResponseEntity<String> handleFileTcPayphone(@RequestParam("file") MultipartFile file) {
        try {
            tcesPayphoneSaveService.saveTces(
                    new TcesPayphoneFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            new TcesPayphoneFromExcelDTO(file)
                    ).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-payphone :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-payphone :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-infomat")
    public ResponseEntity<String> handleFileTcInfomat(@RequestParam("file") MultipartFile file) {
        try {
            tcesInfomatSaveService.saveTces(
                    new TcesInfomatFromExcelDTOValidated(
                            repositoryOperator,
                            repositoryLocation,
                            new TcesInfomatFromExcelDTO(file)
                    ).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-infomat :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/tc-infomat :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
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
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/access-point")
    public ResponseEntity<String> handleFileAccessPoint(@RequestParam("file") MultipartFile file) {
        try {
            apesSaveService.save(
                    new ApesFromExcelDTOValidated(
                            repositoryOrganization,
                            repositoryInternetAccessType,
                            repositorySmoType,
                            repositoryOrganizationType,
                            repositoryLocation, new ApesFromExcelDTO(file)
                    ).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/access-point :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/access-point :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/trunk-channel")
    public ResponseEntity<ByteArrayResource> handleFileTrunkChannel(@RequestParam("file") MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        try {
            ImportResultTrunkChannel importResult = new TrunkChannelsFromExcelDTOValidated(
                    repositoryLocation,
                    repositoryOperator,
                    repositoryTypeTruncChannel,
                    new TrunkChannelsFromExcelDTO(file),
                    repositoryGovernmentDevelopmentProgram).getTcesDTO();
            trunkChannelsSaveService.save(importResult.getListToImport());
            if (importResult.getImportFailure() > 0) {
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/trunk-channel :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/trunk-channel :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/trunk-channel :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/trunk-channel :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/base-station")
    public ResponseEntity<String> handleFileBaseStation(@RequestParam("file") MultipartFile file) {
        try {
            baseStationsSaveService.save(
                    new BaseStationsFromExcelDTOValidated(
                            new BaseStationsFromExcelDTO(file),
                            repositoryOperator,
                            repositoryMobileType).getTcesDTO()
            );
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/base-station :: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("POST /api/import/base-station :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename() + " был успешно импортирован.");
    }
}
