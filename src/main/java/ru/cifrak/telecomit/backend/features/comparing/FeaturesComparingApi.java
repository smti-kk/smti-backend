package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/features-comparing")
public interface FeaturesComparingApi {

    @GetMapping
    Page<LocationFC> locations(Pageable pageable);

    @GetMapping("/{type}")
    Page<LocationFC> locations(
            Pageable pageable,
            @RequestParam(value = "parents", required = false) List<Integer> parentIds,
            @RequestParam(value = "locationName", required = false) String locationName,
            @RequestParam(value = "operators", required = false) List<Integer> internetOperators,
            @RequestParam(value = "connectionTypes", required = false) List<Integer> connectionTypes,
            @RequestParam(value = "govProgram", required = false) Integer govProgram,
            @RequestParam(value = "govProgramYear", required = false) Integer govProgramYear,
            @RequestParam(value = "govProgramYear", required = false) Integer hasAnyInternet,
            @PathVariable(value = "type") String type
    );

    @GetMapping("/{type}/export-excel")
    ResponseEntity<ByteArrayResource> locations(
            @RequestParam(value = "parents", required = false) List<Integer> parentIds,
            @RequestParam(value = "locationName", required = false) String locationName,
            @RequestParam(value = "operators", required = false) List<Integer> internetOperators,
            @RequestParam(value = "connectionTypes", required = false) List<Integer> connectionTypes,
            @RequestParam(value = "govProgram", required = false) Integer govProgram,
            @RequestParam(value = "govProgramYear", required = false) Integer govProgramYear,
            @RequestParam(value = "govProgramYear", required = false) Integer hasAnyInternet,
            @PathVariable(value = "type") String type
    ) throws IOException;

    @PostMapping("/{locationId}/{featureId}/activation")
    void makeItActive(@PathVariable Integer locationId, @PathVariable Integer featureId);
}
