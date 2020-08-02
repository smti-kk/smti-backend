package ru.cifrak.telecomit.backend.api.location;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationFeaturesSaveRequest;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationFeaturesRequests;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

@RestController
public class ApiLocationFeaturesImpl implements ApiLocationFeatures {

    private final ServiceWritableTc serviceWritableTc;
    private final RepositoryLocationFeaturesRequests featuresRequests;

    public ApiLocationFeaturesImpl(ServiceWritableTc serviceWritableTc,
                                   RepositoryLocationFeaturesRequests featuresRequests) {
        this.serviceWritableTc = serviceWritableTc;
        this.featuresRequests = featuresRequests;
    }


    @Override
    @Transactional
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public void saveFeatures(LocationFeaturesSaveRequest request,
                             Integer locationId,
                             @AuthenticationPrincipal User user) {
        LocationFeaturesEditingRequest eReq = new LocationFeaturesEditingRequest(
                locationId,
                request.getComment(),
                user,
                serviceWritableTc.defineEditActions(request.getFeatures(), locationId)
        );
        LocationFeaturesEditingRequest savedRequest = featuresRequests.save(eReq);
        savedRequest.accept(serviceWritableTc);
        featuresRequests.save(savedRequest);
    }
}
