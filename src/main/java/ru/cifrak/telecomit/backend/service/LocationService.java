package ru.cifrak.telecomit.backend.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationParent;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class LocationService {
    private final DSLDetailLocation repository;

    @PersistenceContext
    private EntityManager entityManager;

    public LocationService(DSLDetailLocation repository) {
        this.repository = repository;
    }

    public List<LocationParent> parents() {
        QLocationParent parent = QLocationParent.locationParent;
        return new JPAQuery<LocationParent>(entityManager)
                .from(parent)
                .where(parent.level.eq(1))
                .fetch();
    }

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
}
