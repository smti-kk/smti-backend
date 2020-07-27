package ru.cifrak.telecomit.backend.api.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    private final LocationService locationService;
    private final DSLDetailLocation repository;
    private final RepositoryWritableTc rWritableTc;

    @PersistenceContext
    private EntityManager entityManager;

    public ApiLocationDetailImpl(LocationService locationService, DSLDetailLocation repository, RepositoryWritableTc rWritableTc) {
        this.locationService = locationService;
        this.repository = repository;
        this.rWritableTc = rWritableTc;
    }

    @Override
    public Page<LocationForTable> getList(Pageable pageable,
                                          @Nullable List<Integer> mobileTypes,
                                          @Nullable List<Integer> internetTypes,
                                          @Nullable List<Integer> internetOperators,
                                          @Nullable List<Integer> cellularOperators,
                                          Boolean isLogicalOr,
                                          @Nullable String location,
                                          @Nullable String parent) {
        return locationService.listFiltered(
                pageable,
                mobileTypes,
                internetTypes,
                internetOperators,
                cellularOperators,
                isLogicalOr,
                location,
                parent
        );
    }

    @Override
    public List<LocationParent> parents() {
        return locationService.parents();
    }

    public LocationForTable getOne(Integer id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void save(List<WritableTc> writableTcs, Integer locationId) {
        rWritableTc.saveAll(writableTcs);
    }
}
