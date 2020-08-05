package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;

import java.util.List;

@RequestMapping("/api/features-requests")
public interface ApiFeaturesRequests {
    @GetMapping("/{locationId}")
    List<LocationFeaturesEditingRequestFull> requests(@PathVariable Integer locationId);
}
