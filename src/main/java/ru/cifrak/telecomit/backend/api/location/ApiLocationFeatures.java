package ru.cifrak.telecomit.backend.api.location;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cifrak.telecomit.backend.api.dto.LocationFeaturesSaveRequest;
import ru.cifrak.telecomit.backend.entities.User;


@RequestMapping("/api/location-features")
public interface ApiLocationFeatures {

    @PostMapping("{locationId}")
    void saveFeatures(@RequestBody LocationFeaturesSaveRequest request,
                      @PathVariable Integer locationId,
                      @AuthenticationPrincipal User user);
}
