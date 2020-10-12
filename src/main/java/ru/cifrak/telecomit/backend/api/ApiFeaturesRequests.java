package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable);

    @GetMapping("/{locationId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    List<LocationFeaturesEditingRequestFull> requestsByLocation(@PathVariable Integer locationId);

    @GetMapping("/by-user")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    Page<LocationFeaturesEditingRequestFull> requestsByUser(Pageable pageable, @AuthenticationPrincipal User user);

    @GetMapping("/{request}/accept")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    void acceptRequest(@PathVariable LocationFeaturesEditingRequest request);

    @GetMapping(value = "/{request}/decline", params = {"comment"})
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    void declineRequest(@PathVariable LocationFeaturesEditingRequest request, @RequestParam String comment);
}
