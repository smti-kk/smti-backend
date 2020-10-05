package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable) {
        log.info("->GET /api/features-requests/");
        log.info("<- GET /api/features-requests/");
        return repositoryFeaturesRequests.findAllByOrderByCreatedDesc(pageable);
    }

    @Override
    public List<LocationFeaturesEditingRequestFull> requestsByLocation(Integer locationId) {
        log.info("->GET /api/features-requests/::{}", locationId);
        log.info("<- GET /api/features-requests/::{}", locationId);
        return repositoryFeaturesRequests.findAllByLocationIdOrderByCreatedDesc(locationId);
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requestsByUser(Pageable pageable, User user) {
        log.info("->GET /api/features-requests/by-user");
        log.info("<- GET /api/features-requests/by-user");
        return repositoryFeaturesRequests.findAllByUserOrderByCreatedDesc(user, pageable);
    }

    @Override
    public void acceptRequest(LocationFeaturesEditingRequest request) {
        log.info("->GET /api/features-requests/{request}/accept");
        log.info("<- GET /api/features-requests/{request}/accept");
        request.accept(serviceWritableTc);
        repositoryLocationFeaturesRequests.save(request);
    }

    @Override
    public void declineRequest(LocationFeaturesEditingRequest request, String comment) {
        log.info("->GET /api/features-requests/{request}/decline");
        log.info("<- GET /api/features-requests/{request}/decline");
        request.decline(comment);
        repositoryLocationFeaturesRequests.save(request);
    }
}
