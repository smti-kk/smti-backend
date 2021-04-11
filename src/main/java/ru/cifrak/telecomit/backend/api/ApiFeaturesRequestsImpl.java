package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryAccount;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequest;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.RepositoryFeaturesRequests;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocationFeaturesRequests;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ApiFeaturesRequestsImpl implements ApiFeaturesRequests {
    private final RepositoryFeaturesRequests repositoryFeaturesRequests;
    private final RepositoryAccount repositoryAccount;
    private final RepositoryLocation repositoryLocation;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;
    private final LocationService locationService;

    public ApiFeaturesRequestsImpl(RepositoryFeaturesRequests repositoryFeaturesRequests,
                                   RepositoryAccount repositoryAccount,
                                   RepositoryLocation repositoryLocation,
                                   RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
                                   ServiceWritableTc serviceWritableTc,
                                   LocationService locationService) {
        this.repositoryFeaturesRequests = repositoryFeaturesRequests;
        this.repositoryAccount = repositoryAccount;
        this.repositoryLocation = repositoryLocation;
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
        List<Location> locationsByUser =
                repositoryAccount.findById(user.getId()).orElseThrow(NotFoundException::new)
                        .getLocations().stream().map(l ->
                        repositoryLocation.get(l.getId())).collect(Collectors.toList());
//        List<LocationForTable> locationsByUser =
//                repositoryAccount.findById(user.getId()).orElseThrow(NotFoundException::new)
//                        .getLocations().stream().map(l ->
//                        locationService.getOne(l.getId())).collect(Collectors.toList());
        return repositoryFeaturesRequests.findByLocation_IdIn(locationsByUser, pageable);
    }

    @Override
    public void acceptRequest(LocationFeaturesEditingRequest request, User user) {
        log.info("->GET /api/features-requests/{request}/accept");
        log.info("<- GET /api/features-requests/{request}/accept");
        request.accept(serviceWritableTc);
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
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }
}
