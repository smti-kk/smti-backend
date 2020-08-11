package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/features-comparing")
public interface FeaturesComparingApi {

    @GetMapping
    List<LocationFC> locations();

    @PostMapping("/{locationId}/{featureId}/activation")
    void makeItActive(@PathVariable Integer locationId, @PathVariable Integer featureId);
}
