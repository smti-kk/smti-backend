package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.TcType;

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
            @RequestParam(value = "operators", required = false) List<Integer> internetOperators,
            @RequestParam(value = "connectionTypes", required = false) List<Integer> connectionTypes,
            @RequestParam(value = "govProgram", required = false) Integer govProgram,
            @RequestParam(value = "govProgramYear", required = false) Integer govProgramYear,
            @RequestParam(value = "hasAny", required = false) Integer hasAny,
            @RequestParam(value = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @PathVariable(value = "type") TcType type,
            @RequestParam(value = "locationName", required = false) String... locationNames
    );

    @GetMapping("/{type}/export-excel")
    ResponseEntity<ByteArrayResource> locations(
            @RequestParam(value = "parents", required = false) List<Integer> parentIds,
            @RequestParam(value = "operators", required = false) List<Integer> internetOperators,
            @RequestParam(value = "connectionTypes", required = false) List<Integer> connectionTypes,
            @RequestParam(value = "govProgram", required = false) Integer govProgram,
            @RequestParam(value = "govProgramYear", required = false) Integer govProgramYear,
            @RequestParam(value = "hasAny", required = false) Integer hasAny,
            @RequestParam(value = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @PathVariable(value = "type") TcType type,
            @RequestParam(value = "locationName", required = false) String... locationNames
    ) throws IOException;

    @PostMapping("/{locationId}/{featureId}/activation")
    void makeItActive(@PathVariable Integer locationId, @PathVariable Integer featureId);
}
