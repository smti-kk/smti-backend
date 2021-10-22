package ru.cifrak.telecomit.backend.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/features-requests")
public interface ApiFeaturesRequests {

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable);

    @GetMapping("/full")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    Page<LocationFeaturesEditingRequestFull> requestsAndImportsAndEditions(
            Pageable pageable,
            @RequestParam(value = "sort", required = false) List<String> sort,
            @RequestParam(value = "parent", required = false) List<LocationForTable> parents,
            @RequestParam(value = "contractStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contractStart,
            @RequestParam(value = "contractEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contractEnd,
            @RequestParam(value = "action", required = false) List<FeatureEditAction> actions,
            @RequestParam(value = "user", required = false) List<User> users,
            @RequestParam(value = "location", required = false) List<LocationForTable> locations);

    @GetMapping("/full-excel")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    ResponseEntity<ByteArrayResource> requestsAndImportsAndEditionsExcel(
            @RequestParam(value = "sort", required = false) List<String> sort,
            @RequestParam(value = "parent", required = false) List<LocationForTable> parents,
            @RequestParam(value = "contractStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contractStart,
            @RequestParam(value = "contractEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contractEnd,
            @RequestParam(value = "action", required = false) List<FeatureEditAction> actions,
            @RequestParam(value = "user", required = false) List<User> users,
            @RequestParam(value = "location", required = false) List<LocationForTable> locations);

    @GetMapping("/{locationId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    List<LocationFeaturesEditingRequestFull> requestsByLocation(@PathVariable Integer locationId);

    @GetMapping("/by-user")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    Page<LocationFeaturesEditingRequestFull> requestsByUser(Pageable pageable,
                                                            @AuthenticationPrincipal User user);

    @GetMapping("/{request}/accept")
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    void acceptRequest(@PathVariable LocationFeaturesEditingRequest request,
                       @AuthenticationPrincipal User user);

    @GetMapping(value = "/{request}/decline", params = {"comment"})
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    void declineRequest(@PathVariable LocationFeaturesEditingRequest request,
                        @RequestParam String comment,
                        @AuthenticationPrincipal User user);
}
