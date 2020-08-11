package ru.cifrak.telecomit.backend.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;

import java.util.List;

@RequestMapping("/api/features-requests")
public interface ApiFeaturesRequests {

    @GetMapping
    List<LocationFeaturesEditingRequestFull> requests();

    @GetMapping("/{locationId}")
    List<LocationFeaturesEditingRequestFull> requestsByLocation(@PathVariable Integer locationId);

    @GetMapping("/by-user")
    List<LocationFeaturesEditingRequestFull> requestsByUser(@AuthenticationPrincipal User user);

    @GetMapping("/{request}/accept")
    void acceptRequest(@PathVariable LocationFeaturesEditingRequest request);

    @GetMapping(value = "/{request}/decline", params = {"comment"})
    void declineRequest(@PathVariable LocationFeaturesEditingRequest request, @RequestParam String comment);
}
