package ru.cifrak.telecomit.backend.api.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.ExelReportAccessPointFullDTO;
import ru.cifrak.telecomit.backend.api.dto.ExelReportLocation;
import ru.cifrak.telecomit.backend.api.dto.LocationProvidingInfo;
import ru.cifrak.telecomit.backend.api.util.Reports.HelperReport;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.cifrak.telecomit.backend.api.util.Reports.HelperReport.generateExelFormat;
import static ru.cifrak.telecomit.backend.api.util.Reports.HelperReport.generateExelFormatLocationType;

@Slf4j
@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
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
    public Page<LocationForTable> getList(Pageable pageable,
                                          @Nullable List<Integer> mobileTypes,
                                          @Nullable List<Integer> internetTypes,
                                          @Nullable List<Integer> internetOperators,
                                          @Nullable List<Integer> cellularOperators,
                                          Boolean isLogicalOr,
                                          @Nullable String location,
                                          @Nullable String parent) {
        return locationService.listFiltered(
                pageable,
                mobileTypes,
                internetTypes,
                internetOperators,
                cellularOperators,
                isLogicalOr,
                location,
                parent
        );
    }

    @Override
    public List<LocationParent> parents() {
        return locationService.parents();
    }

    public LocationForTable getOne(Integer id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public LocationProvidingInfo locationProvidingInfo(Integer locationId) {
        return new LocationProvidingInfo(repository.getOne(locationId));
    }

    public List<Integer> govProgramYears() {
        return writableTcRepo.existGovCompleteYears();
    }

    public ResponseEntity<ByteArrayResource> exportExcel(List<Integer> locationIds) throws IOException {
        List<Location> allById = repositoryLocation.findAllById(locationIds);

        List<ExelReportLocation> collect = allById
                .stream()
                .map(ExelReportLocation::new)
                .collect(Collectors.toList());

        IntStream.range(0, collect.size()).forEach(i -> collect.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFormatLocationType().exportToByteArray(collect));

        log.info("<-GET /api/report/organization/ap-all/export");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%D0%9E%D1%82%D1%87%D1%91%D1%82%20%D0%BC%D0%BE%D0%BD%D0%B8%D1%82%D0%BE%D1%80%D0%B8%D0%BD%D0%B3%D0%B0%20%D0%B7%D0%B0%20" + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    public List<LocationForTable> byUser(User user) {
        return repository.findByUserId(user.getId());
    }
}
