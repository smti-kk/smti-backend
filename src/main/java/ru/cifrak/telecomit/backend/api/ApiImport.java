package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.dto.DtoImportAccessPointParams;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApesFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApesFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.ap.ApesSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.basestation.BaseStationsSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.location.LocationsSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcAtsImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcesAtsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcesAtsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcats.TcesAtsSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcInfomatImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcesInfomatFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcesInfomatFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcinfomat.TcesInfomatSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcInternetImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcinternet.TcesInternetSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcMobileImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcesMobileFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcesMobileFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcmobile.TcesMobileSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcPayphoneImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcesPayphoneFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcesPayphoneFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcpayphone.TcesPayphoneSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcPostImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcpost.TcesPostSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcRadioImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tcradio.TcesRadioSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcTvImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.tctv.TcesTvSaveService;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelImportResult;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelsFromExcelDTO;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelsFromExcelDTOValidated;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelsSaveService;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.repository.*;

import javax.validation.Valid;
import java.io.IOException;

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

    private final RepositoryAccessPoints repositoryAccessPoints;

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
            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram,
            RepositoryAccessPoints repositoryAccessPoints) {
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
        this.repositoryAccessPoints = repositoryAccessPoints;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/location")
    public ResponseEntity<ByteArrayResource> handleFileLocation(@RequestParam("file") MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        try {
            LocationImportResult importResult = new LocationsFromExcelDTOValidated(
                    repositoryLocation, new LocationsFromExcelDTO(file)).getTcesDTO();
            locationsSaveService.save(importResult.getListToImport());
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/location :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/location :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/location :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-internet")
    public ResponseEntity<ByteArrayResource> handleFileTcInternet(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcInternetImportResult importResult = new TcesInternetFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    repositoryTypeTruncChannel,
                    new TcesInternetFromExcelDTO(file)).getTcesDTO();
            tcesInternetSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-internet :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-internet :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-internet :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-mobile")
    public ResponseEntity<ByteArrayResource> handleFileTcMobile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcMobileImportResult importResult = new TcesMobileFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    repositoryMobileType,
                    new TcesMobileFromExcelDTO(file)).getTcesDTO();
            tcesMobileSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-mobile :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-mobile :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-mobile :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-mobile :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-mobile :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-payphone")
    public ResponseEntity<ByteArrayResource> handleFileTcPayphone(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcPayphoneImportResult importResult = new TcesPayphoneFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    new TcesPayphoneFromExcelDTO(file)).getTcesDTO();
            tcesPayphoneSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-payphone :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-payphone :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-payphone :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-payphone :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-payphone :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-infomat")
    public ResponseEntity<ByteArrayResource> handleFileTcInfomat(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcInfomatImportResult importResult = new TcesInfomatFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    new TcesInfomatFromExcelDTO(file)).getTcesDTO();
            tcesInfomatSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-infomat :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-infomat :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-infomat :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-infomat :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-infomat :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-ats")
    public ResponseEntity<ByteArrayResource> handleFileTcAts(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcAtsImportResult importResult = new TcesAtsFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    new TcesAtsFromExcelDTO(file)).getTcesDTO();
            tcesAtsSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-ats :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-ats :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-ats :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-ats :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-ats :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-tv")
    public ResponseEntity<ByteArrayResource> handleFileTcTv(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcTvImportResult importResult = new TcesTvFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    new TcesTvFromExcelDTO(file)).getTcesDTO();
            tcesTvSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-tv :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-tv :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-tv :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-tv :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-tv :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-radio")
    public ResponseEntity<ByteArrayResource> handleFileTcRadio(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcRadioImportResult importResult = new TcesRadioFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    new TcesRadioFromExcelDTO(file)).getTcesDTO();
            tcesRadioSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-radio :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-radio :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-radio :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-radio :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-radio :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/tc-post")
    public ResponseEntity<ByteArrayResource> handleFileTcPost(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TcPostImportResult importResult = new TcesPostFromExcelDTOValidated(
                    repositoryOperator,
                    repositoryLocation,
                    new TcesPostFromExcelDTO(file)).getTcesDTO();
            tcesPostSaveService.save(importResult.getListToImport(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/tc-post :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-post :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/tc-post :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/tc-post :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/tc-post :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/access-point")
    public ResponseEntity<ByteArrayResource> handleFileAccessPoint(@Valid DtoImportAccessPointParams params,
                                                                   @AuthenticationPrincipal User user
                                                                   ) {
        HttpHeaders headers = new HttpHeaders();
        try {
            ApImportResult importResult = new ApesFromExcelDTOValidated(
                    repositoryOrganization,
                    repositoryInternetAccessType,
                    repositorySmoType,
                    repositoryOrganizationType,
                    repositoryLocation,
                    repositoryGovernmentDevelopmentProgram,
                    repositoryAccessPoints,
                    // TODO: вернуть apType в качестве аргумента для двух методов ниже
                    new ApesFromExcelDTO(params.getFile())).getTcesDTO(params.getApType());
            apesSaveService.save(importResult.getListToImport(), params.getApType(), user);
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/access-point :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("import-message: format-error");
            log.error("<-POST /api/import/access-point :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("import-message: npp-error");
            log.error("<-POST /api/import/access-point :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (IOException e) {
            log.error("import-message: input/output-error");
            log.error("<-POST /api/import/access-point :: {}", e.getMessage());
            headers.set("import-message", "input/output-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("import-message: unexpected");
            log.error("<-POST /api/import/access-point :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/access-point :: {}", params.getFile().getOriginalFilename());
        return ResponseEntity.ok(null);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/trunk-channel")
    public ResponseEntity<ByteArrayResource> handleFileTrunkChannel(@RequestParam("file") MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        try {
            TrunkChannelImportResult importResult = new TrunkChannelsFromExcelDTOValidated(
                    repositoryLocation,
                    repositoryOperator,
                    repositoryTypeTruncChannel,
                    new TrunkChannelsFromExcelDTO(file),
                    repositoryGovernmentDevelopmentProgram).getTcesDTO();
            trunkChannelsSaveService.save(importResult.getListToImport());
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/trunk-channel :: error");
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
    public ResponseEntity<ByteArrayResource> handleFileBaseStation(@RequestParam("file") MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        try {
            BaseStationImportResult importResult = new BaseStationsFromExcelDTOValidated(
                    new BaseStationsFromExcelDTO(file),
                    repositoryOperator,
                    repositoryMobileType).getTcesDTO();
            baseStationsSaveService.save(importResult.getListToImport());
            if (importResult.getImportFailure() > 0) {
                log.error("<-POST /api/import/base-station :: error");
                headers.set("import-success", String.valueOf(importResult.getImportSuccess()));
                headers.set("import-failure", String.valueOf(importResult.getImportFailure()));
                headers.set("import-message", "error");
                return ResponseEntity.badRequest().headers(headers)
                        .body(importResult.getFileWithError());
            }
        } catch (FromExcelDTOFormatException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/base-station :: {}", e.getMessage());
            headers.set("import-message", "format-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (FromExcelDTONppException e) {
            // TODO: <-, -> ?
            log.error("<-POST /api/import/base-station :: {}", e.getMessage());
            headers.set("import-message", "npp-error");
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception e) {
            log.error("<-POST /api/import/base-station :: {}", e.getMessage());
            headers.set("import-message", "unexpected");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }
        log.info("POST /api/import/base-station :: {}", file.getOriginalFilename());
        return ResponseEntity.ok(null);
    }
}
