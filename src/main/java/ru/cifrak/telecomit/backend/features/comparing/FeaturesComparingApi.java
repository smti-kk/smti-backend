package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/features-comparing")
public interface FeaturesComparingApi {

    @GetMapping
    Page<LocationFC> locations(Pageable pageable);

    @PostMapping("/{locationId}/{featureId}/activation")
    void makeItActive(@PathVariable Integer locationId, @PathVariable Integer featureId);
}
