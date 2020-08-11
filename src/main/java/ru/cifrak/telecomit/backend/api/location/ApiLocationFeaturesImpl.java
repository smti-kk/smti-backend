package ru.cifrak.telecomit.backend.api.location;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationFeaturesSaveRequest;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.repository.RepositoryFeatureEdits;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationFeaturesRequests;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

@RestController
public class ApiLocationFeaturesImpl implements ApiLocationFeatures {

    private final ServiceWritableTc serviceWritableTc;
    private final RepositoryWritableTc rWritableTc;
    private final RepositoryLocationFeaturesRequests featuresRequests;
    private final RepositoryFeatureEdits repositoryFeatureEdits;

    public ApiLocationFeaturesImpl(ServiceWritableTc serviceWritableTc,
                                   RepositoryWritableTc rWritableTc,
                                   RepositoryLocationFeaturesRequests featuresRequests,
                                   RepositoryFeatureEdits repositoryFeatureEdits) {
        this.serviceWritableTc = serviceWritableTc;
        this.rWritableTc = rWritableTc;
        this.featuresRequests = featuresRequests;
        this.repositoryFeatureEdits = repositoryFeatureEdits;
    }


    @Override
    @Transactional
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public void saveFeatures(LocationFeaturesSaveRequest request,
                             Integer locationId,
                             @AuthenticationPrincipal User user) {
        LocationFeaturesEditingRequest eReq = new LocationFeaturesEditingRequest(
                locationId,
                request.getComment(),
                user,
                serviceWritableTc.defineEditActions(request.getFeatures(), locationId)
        );
        eReq.getFeatureEdits().forEach(fe -> {
            if (fe.getTc() != null) {
                rWritableTc.save(fe.getTc());
            }
            if (fe.getNewValue() != null) {
                rWritableTc.save(fe.getNewValue());
            }
        });
        repositoryFeatureEdits.saveAll(eReq.getFeatureEdits());
        LocationFeaturesEditingRequest savedRequest = featuresRequests.save(eReq);
        if (!user.getRoles().contains(UserRole.MUNICIPALITY) &&
                (user.getRoles().contains(UserRole.OPERATOR) || user.getRoles().contains(UserRole.ADMIN))
        ) {
            savedRequest.accept(serviceWritableTc);
            featuresRequests.save(savedRequest);
        }
    }
}
