package ru.cifrak.telecomit.backend.api.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationProvidingInfo;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    private final LocationService locationService;
    private final DSLDetailLocation repository;
    private final RepositoryWritableTc writableTcRepo;

    public ApiLocationDetailImpl(LocationService locationService,
                                 DSLDetailLocation repository,
                                 RepositoryWritableTc writableTcRepo) {
        this.locationService = locationService;
        this.repository = repository;
        this.writableTcRepo = writableTcRepo;
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

    public LocationForTable getOne(Integer id) throws NotFoundException {
        return locationService.getOne(id);
    }

    public LocationProvidingInfo locationProvidingInfo(Integer locationId) {
        return new LocationProvidingInfo(repository.getOne(locationId));
    }

    public List<Integer> govProgramYears() {
        return writableTcRepo.existGovCompleteYears();
    }

    public void exportExcel(List<Integer> locationIds) {
        List<LocationForTable> allById = repository.findAllById(locationIds);
        // todo: implement me pls
    }

    public List<LocationForTable> byUser(User user) {
        return repository.findByUserId(user.getId());
    }
}
