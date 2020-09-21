package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.FeatureExportDTO;
import ru.cifrak.telecomit.backend.entities.ExcelExportTypes;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.TcType;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ReportName;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.cifrak.telecomit.backend.api.util.Reports.HelperReport.generateExelFeatureReport;

@RestController
public class FeaturesComparingApiImpl implements FeaturesComparingApi {

    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final FeatureComparingService featureComparingService;
    private final RepositoryWritableTc repositoryWritableTc;

    public FeaturesComparingApiImpl(LocationRepository locationRepository,
                                    LocationService locationService,
                                    FeatureComparingService featureComparingService,
                                    RepositoryWritableTc repositoryWritableTc) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        this.featureComparingService = featureComparingService;
        this.repositoryWritableTc = repositoryWritableTc;
    }

    @Override
    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    public Page<LocationFC> locations(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    @Override
    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    public Page<LocationFC> locations(
            Pageable pageable,
            List<Integer> parentIds,
            String locationName,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAnyInternet,
            TcType type
    ) {
        if (type.equals(TcType.INET)) {
            return featureComparingService.locations(
                    pageable,
                    parentIds,
                    locationName,
                    operators,
                    null,
                    connectionTypes,
                    null,
                    govProgram,
                    govProgramYear,
                    hasAnyInternet,
                    null
            );
        } else if (type.equals(TcType.MOBILE)) {
            return featureComparingService.locations(
                    pageable,
                    parentIds,
                    locationName,
                    null,
                    operators,
                    null,
                    connectionTypes,
                    govProgram,
                    govProgramYear,
                    null,
                    hasAnyInternet
            );
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> locations(
            List<Integer> parentIds,
            String locationName,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAnyInternet,
            TcType type
    ) throws IOException {
        List<LocationFC> locationFCS;
        if (type.equals(TcType.INET)) {
            locationFCS = featureComparingService.locations(
                    parentIds,
                    locationName,
                    operators,
                    null,
                    connectionTypes,
                    null,
                    govProgram,
                    govProgramYear,
                    hasAnyInternet,
                    null
            );
        } else if (type.equals(TcType.MOBILE)) {
            locationFCS = featureComparingService.locations(
                    parentIds,
                    locationName,
                    null,
                    operators,
                    null,
                    connectionTypes,
                    govProgram,
                    govProgramYear,
                    null,
                    hasAnyInternet
            );
        } else {
            throw new IllegalArgumentException("Unsupported type " + type);
        }
        List<FeatureExportDTO> collect = locationFCS
                .stream()
                .map(str -> new FeatureExportDTO(str, type))
                .collect(Collectors.toList());
        IntStream.range(0, collect.size()).forEach(i -> collect.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFeatureReport(type).exportToByteArray(collect));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, new ReportName(ExcelExportTypes.fromTcType(type)).toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @Override
    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    public void makeItActive(Integer locationId, Integer featureId) {
        WritableTc feature = repositoryWritableTc.getOne(featureId);
        WritableTc activeFeature = repositoryWritableTc.findByLocationIdAndStateAndOperatorId(
                locationId,
                TcState.ACTIVE,
                feature.getOperatorId()
        );
        feature.setState(TcState.ACTIVE);
        if (activeFeature != null) {
            activeFeature.setState(TcState.ARCHIVE);
            repositoryWritableTc.save(activeFeature);
        }
        repositoryWritableTc.save(feature);
        locationService.refreshCache();
    }

}
