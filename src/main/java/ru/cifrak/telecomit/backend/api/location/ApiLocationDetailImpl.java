package ru.cifrak.telecomit.backend.api.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.annotation.Nullable;
import java.util.List;

@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    private final LocationService locationService;
    private final DSLDetailLocation repository;

    public ApiLocationDetailImpl(LocationService locationService, DSLDetailLocation repository) {
        this.locationService = locationService;
        this.repository = repository;
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

    @Transactional
    public LocationForTable getOne(Integer id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }
}
