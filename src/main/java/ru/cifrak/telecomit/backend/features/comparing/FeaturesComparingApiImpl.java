package ru.cifrak.telecomit.backend.features.comparing;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.FeatureExportDTO;
import ru.cifrak.telecomit.backend.entities.ExcelExportTypes;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
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

import static ru.cifrak.telecomit.backend.api.util.reports.HelperReport.generateExelFeatureReport;

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
        return locationRepository.findAll(
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("locationParent.name").ascending().and(Sort.by("name").ascending())));
    }

    @Override
    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    public Page<LocationFC> locations(
            Pageable pageable,
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Boolean hasAny,
            LogicalCondition logicalCondition,
            TcType type,
            String... locationNames
    ) {
        return featureComparingService.locations(
                pageable,
                parentIds,
                operators,
                connectionTypes,
                govProgram,
                govProgramYear,
                getHasAnyNotNull(hasAny),
                type,
                logicalCondition,
                locationNames);
    }

    private boolean getHasAnyNotNull(Boolean hasAny) {
        return hasAny != null ? hasAny : false;
    }

    @Override
    public ResponseEntity<ByteArrayResource> locations(
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Boolean hasAny,
            LogicalCondition logicalCondition,
            TcType type,
            String... locationNames
    ) throws IOException {
        ByteArrayResource resource = getResource(
                parentIds,
                operators,
                connectionTypes,
                govProgram,
                govProgramYear,
                getHasAnyNotNull(hasAny),
                type,
                logicalCondition,
                locationNames);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, new ReportName(ExcelExportTypes.fromTcType(type)).toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @NotNull
    private ByteArrayResource getResource(
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Boolean hasAny,
            TcType type,
            LogicalCondition logicalCondition,
            String... locationNames
    ) throws IOException {
        List<FeatureExportDTO> collect = featureComparingService.locations(
                        parentIds,
                        operators,
                        connectionTypes,
                        govProgram,
                        govProgramYear,
                        hasAny,
                        type,
                        logicalCondition,
                        locationNames
                )
                .stream()
                .map(str -> new FeatureExportDTO(str, type))
                .collect(Collectors.toList());
        IntStream.range(0, collect.size()).forEach(i -> collect.get(i).setPp(i + 1));
        return new ByteArrayResource(generateExelFeatureReport(type).exportToByteArray(collect));
    }

    @Override
    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    public void makeItActive(Integer locationId, Integer featureId) {
        WritableTc feature = repositoryWritableTc.getOne(featureId);
        WritableTc activeFeature = repositoryWritableTc.findByLocationIdAndStateAndOperatorIdAndType(
                locationId,
                TcState.ACTIVE,
                feature.getOperatorId(),
                feature.getType()
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
