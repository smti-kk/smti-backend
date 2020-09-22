package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;
import ru.cifrak.telecomit.backend.repository.RepositoryFeaturesRequests;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationFeaturesRequests;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import java.util.List;

@RestController
public class ApiFeaturesRequestsImpl implements ApiFeaturesRequests {
    private final RepositoryFeaturesRequests repositoryFeaturesRequests;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;

    public ApiFeaturesRequestsImpl(RepositoryFeaturesRequests repositoryFeaturesRequests,
                                   RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
                                   ServiceWritableTc serviceWritableTc) {
        this.repositoryFeaturesRequests = repositoryFeaturesRequests;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableTc = serviceWritableTc;
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable) {
        return repositoryFeaturesRequests.findAllByOrderByCreatedDesc(pageable);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public List<LocationFeaturesEditingRequestFull> requestsByLocation(Integer locationId) {
        return repositoryFeaturesRequests.findAllByLocationIdOrderByCreatedDesc(locationId);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public Page<LocationFeaturesEditingRequestFull> requestsByUser(Pageable pageable, User user) {
        return repositoryFeaturesRequests.findAllByUserOrderByCreatedDesc(user, pageable);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public void acceptRequest(LocationFeaturesEditingRequest request) {
        request.accept(serviceWritableTc);
        repositoryLocationFeaturesRequests.save(request);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public void declineRequest(LocationFeaturesEditingRequest request, String comment) {
        request.decline(comment);
        repositoryLocationFeaturesRequests.save(request);
    }
}
