package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;
import ru.cifrak.telecomit.backend.repository.RepositoryFeaturesRequests;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationFeaturesRequests;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
public class ApiFeaturesRequestsImpl implements ApiFeaturesRequests {
    private final RepositoryFeaturesRequests repositoryFeaturesRequests;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;
    private final LocationService locationService;

    public ApiFeaturesRequestsImpl(RepositoryFeaturesRequests repositoryFeaturesRequests,
                                   RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
                                   ServiceWritableTc serviceWritableTc,
                                   LocationService locationService) {
        this.repositoryFeaturesRequests = repositoryFeaturesRequests;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableTc = serviceWritableTc;
        this.locationService = locationService;
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable) {
        log.info("->GET /api/features-requests/");
        log.info("<- GET /api/features-requests/");
        return repositoryFeaturesRequests.findAllRequests(pageable);
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requestsAndImportsAndEditions(Pageable pageable) {
        log.info("->GET /api/features-requests/full/");
        log.info("<- GET /api/features-requests/full/");
        return repositoryFeaturesRequests.findAllRequestsAndImportAndEditions(pageable);
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
    public void acceptRequest(LocationFeaturesEditingRequest request, User user) {
        log.info("->GET /api/features-requests/{request}/accept");
        log.info("<- GET /api/features-requests/{request}/accept");
        String comment = "";
        if (request.getComment() != null && request.getComment().length() > 0) {
            comment += " с комментарием \"" + request.getComment() + "\"";
        }
        request.accept(serviceWritableTc);
        request.setComment(MessageFormat.format("Заявка пользователя {0} {2} подтверждена оператором {1}",
                request.getUser().getUsername(),
                user.getUsername(),
                comment));
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }

    @Override
    public void declineRequest(LocationFeaturesEditingRequest request,
                               String comment,
                               User user) {
        log.info("->GET /api/features-requests/{request}/decline");
        log.info("<- GET /api/features-requests/{request}/decline");
        request.decline(comment);
        request.setComment(
                MessageFormat.format("Заявка пользователя {0} отклонена с комментарием оператора {2}: {1}",
                        request.getUser().getUsername(),
                        comment,
                        user.getUsername()));
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }
}
