package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryDetailLocation;

@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    private final RepositoryDetailLocation repositoryDetailLocation;

    public ApiLocationDetailImpl(RepositoryDetailLocation repositoryDetailLocation) {
        this.repositoryDetailLocation = repositoryDetailLocation;
    }

    @Override
    public Page<DetailLocation> getList(Pageable pageable) {
        return repositoryDetailLocation.findAll(pageable);
    }
}
