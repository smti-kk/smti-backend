package ru.cifrak.telecomit.backend.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationParent;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class LocationService {
    private final DSLDetailLocation repository;

    @PersistenceContext
    private EntityManager entityManager;
    private Date lastRefreshDate = new Date();

    public LocationService(DSLDetailLocation repository) {
        this.repository = repository;
    }

    @Cacheable("location_parents")
    public List<LocationParent> parents() {
        QLocationParent parent = QLocationParent.locationParent;
        return new JPAQuery<LocationParent>(entityManager)
                .from(parent)
                .where(parent.level.eq(1))
                .fetch();
    }

    @Cacheable(value = "locations")
    public Page<LocationForTable> listFiltered(Pageable pageable,
                                               @Nullable List<Integer> mobileTypes,
                                               @Nullable List<Integer> internetTypes,
                                               @Nullable List<Integer> internetOperators,
                                               @Nullable List<Integer> cellularOperators,
                                               Boolean isLogicalOr,
                                               @Nullable String location,
                                               @Nullable String parent) {
        BooleanExpression expression = new DetailLocationFilterBuilder()
                .logicalOperation(isLogicalOr)
                .internetTypes(internetTypes)
                .mobileTypes(mobileTypes)
                .internetOperators(internetOperators)
                .cellularOperators(cellularOperators)
                .logicalOperation(false)
                .location(location)
                .parent(parent)
                .build();
        return repository.findAll(expression, pageable);
    }

    @Cacheable("locations")
    public LocationForTable getOne(Integer id) throws NotFoundException {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @CacheEvict(value = {
            "locations",
            "locations_fc",
            "map-locations",
            "location_parents",
            "location_location_parents",
            "grouped_operators",
            "gov_programs",
            "gov_years",
            "location_areas",
            "trunk_channels",
            "type_mobiles"
    }, allEntries = true)
    public void refreshCache() {
        lastRefreshDate = new Date();
    }

    public Date getLastRefreshDate() {
        return lastRefreshDate;
    }
}
