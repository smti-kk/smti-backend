package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationFeaturesEditingRequestFull;
import ru.cifrak.telecomit.backend.repository.RepositoryFeaturesRequests;

import java.util.List;

@RestController
public class ApiFeaturesRequestsImpl implements ApiFeaturesRequests {
    private final RepositoryFeaturesRequests repositoryFeaturesRequests;

    public ApiFeaturesRequestsImpl(RepositoryFeaturesRequests repositoryFeaturesRequests) {
        this.repositoryFeaturesRequests = repositoryFeaturesRequests;
    }

    @Override
    public List<LocationFeaturesEditingRequestFull> requests(Integer locationId) {
        return repositoryFeaturesRequests.findAllByLocationIdOrderByCreatedDesc(locationId);
    }
}
