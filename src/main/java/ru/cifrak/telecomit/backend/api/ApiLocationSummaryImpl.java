package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryDetailLocation;

import java.util.List;

@RestController
public class ApiLocationSummaryImpl implements ApiLocationSummary {
    private final RepositoryDetailLocation repositoryDetailLocation;

    public ApiLocationSummaryImpl(RepositoryDetailLocation repositoryDetailLocation) {
        this.repositoryDetailLocation = repositoryDetailLocation;
    }

    @Override
    public List<DetailLocation> getAll() {
        return repositoryDetailLocation.findAll();
    }
}
