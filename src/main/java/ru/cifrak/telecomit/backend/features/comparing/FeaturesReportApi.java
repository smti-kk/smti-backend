package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/features-comparing")
public interface FeaturesReportApi {
    @GetMapping("/export-excel-internet/")
    ResponseEntity<ByteArrayResource> exportFeatureDataExcelInternet(@RequestBody List<Integer> featureId) throws IOException;
    @GetMapping("/export-excel-telephone/")
    ResponseEntity<ByteArrayResource> exportFeatureDataExcelTelephone(@RequestBody List<Integer> featureId) throws IOException;
}
