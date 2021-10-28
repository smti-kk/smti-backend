package ru.cifrak.telecomit.backend.api.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.ExelReportLocation;
import ru.cifrak.telecomit.backend.api.dto.LocationProvidingInfo;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.exceptions.WrongParentException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.cifrak.telecomit.backend.api.util.reports.HelperReport.generateExelFormatLocationType;

@Slf4j
@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    public static final int KRASNOYARSK_REGION_ID = 4;
    private final LocationService locationService;
    private final DSLDetailLocation repository;
    private final RepositoryLocation repositoryLocation;
    private final RepositoryWritableTc writableTcRepo;

    public ApiLocationDetailImpl(LocationService locationService,
                                 DSLDetailLocation repository,
                                 RepositoryLocation repositoryLocation,
                                 RepositoryWritableTc writableTcRepo) {
        this.locationService = locationService;
        this.repository = repository;
        this.repositoryLocation = repositoryLocation;
        this.writableTcRepo = writableTcRepo;
    }

    @Override
    public ResponseEntity<Page<LocationForTable>> getList(Pageable pageable,
                                                          @Nullable List<Integer> mobileTypes,
                                                          @Nullable List<Integer> internetTypes,
                                                          @Nullable List<Integer> internetOperators,
                                                          @Nullable List<Integer> cellularOperators,
                                                          Boolean isLogicalOr,
                                                          @Nullable String location,
                                                          @Nullable String parent
    ) {
        Page<LocationForTable> result = locationService.listFiltered(
                pageable,
                mobileTypes,
                internetTypes,
                internetOperators,
                cellularOperators,
                isLogicalOr,
                location,
                parent
        );
        CacheControl cacheControl = CacheControl.empty()
                .noTransform()
                .mustRevalidate();
        return ResponseEntity.ok()
                .lastModified(locationService.getLastRefreshDate().getTime())
                .cacheControl(cacheControl)
                .body(result);
    }

    @Override
    public List<LocationParent> parents() {
        return locationService.parents();
    }

    public LocationForTable getOne(Integer id) throws NotFoundException {
        return locationService.getOne(id);
    }

    public LocationProvidingInfo locationProvidingInfo(Integer locationId) {
        return new LocationProvidingInfo(repository.getOne(locationId));
    }

    public List<Integer> govProgramYears() {
        return writableTcRepo.existGovCompleteYears();
    }

    public ResponseEntity<ByteArrayResource> exportExcel(List<Integer> locationIds) throws IOException {
        log.info("->GET /api/detail-locations/export-excel");
        List<Location> allById = repositoryLocation.findAllById(locationIds);

        List<ExelReportLocation> collect = allById
                .stream()
                .map(ExelReportLocation::new)
                .collect(Collectors.toList());

        IntStream.range(0, collect.size()).forEach(i -> collect.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFormatLocationType().exportToByteArray(collect));

        log.info("<-GET /api/detail-locations/export-excel");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%D0%9E%D1%82%D1%87%D0%B5%D1%82_%D0%BF%D0%BE_%D0%BD%D0%B0%D1%81%D0%B5%D0%BB%D0%B5%D0%BD%D0%BD%D1%8B%D0%BC_%D0%BF%D1%83%D0%BD%D0%BA%D1%82%D0%B0%D0%BC" + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    public List<LocationForTable> byUser(User user) {
        return repository.findByUserId(user.getId());
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public void deleteById(Integer id) {
        repository.forceDeleteById(id);
        locationService.refreshCache();
    }

    @DeleteMapping(params = {"id", "population", "area"})
    @Secured({"ROLE_ADMIN"})
    public void update(@RequestParam("id") Integer id,
                       @RequestParam("population") Integer population,
                       @RequestParam("area") Integer area,
                       @AuthenticationPrincipal User user) throws WrongParentException {
        log.info("user {} update location {} with id", user.getEmail(), id);
        if (repository.getOne(id).getLocationParent().getId() == KRASNOYARSK_REGION_ID &&
                area != KRASNOYARSK_REGION_ID) {
            throw new WrongParentException();
        } else {
            repository.updatePopulationAndParent(id, population, area);
        }
        locationService.refreshCache();
    }
}
