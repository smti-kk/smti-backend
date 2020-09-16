package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.cifrak.telecomit.backend.api.dto.FeatureExportDTO;
import ru.cifrak.telecomit.backend.entities.TcType;
import ru.cifrak.telecomit.backend.utils.export.ExcelExporter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.cifrak.telecomit.backend.api.util.Reports.HelperReport.generateExelFeatureReport;

public class FeatureReportImpl implements FeaturesReportApi {

    private final LocationRepository locationRepository;

    public FeatureReportImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportFeatureDataExcelInternet(List<Integer> featureId) throws IOException {
        ExcelExporter<FeatureExportDTO> excelForm = generateExelFeatureReport();
        List<LocationFC> allById = locationRepository.findAllById(featureId);

        List<FeatureExportDTO> collect = allById
                .stream()
                .map(str -> new FeatureExportDTO(str, TcType.INET))
                .collect(Collectors.toList());

        IntStream.range(0, collect.size()).forEach(i -> collect.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFeatureReport().exportToByteArray(collect));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%D0%9E%D1%82%D1%87%D1%91%D1%82%20%D0%BC%D0%BE%D0%BD%D0%B8%D1%82%D0%BE%D1%80%D0%B8%D0%BD%D0%B3%D0%B0%20%D0%B7%D0%B0%20" + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportFeatureDataExcelTelephone(List<Integer> featureId) throws IOException {
        ExcelExporter<FeatureExportDTO> excelForm = generateExelFeatureReport();
        List<LocationFC> allById = locationRepository.findAllById(featureId);

        List<FeatureExportDTO> collect = allById
                .stream()
                .map(str -> new FeatureExportDTO(str, TcType.MOBILE))
                .collect(Collectors.toList());

        IntStream.range(0, collect.size()).forEach(i -> collect.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFeatureReport().exportToByteArray(collect));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%D0%9E%D1%82%D1%87%D1%91%D1%82%20%D0%BC%D0%BE%D0%BD%D0%B8%D1%82%D0%BE%D1%80%D0%B8%D0%BD%D0%B3%D0%B0%20%D0%B7%D0%B0%20" + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
