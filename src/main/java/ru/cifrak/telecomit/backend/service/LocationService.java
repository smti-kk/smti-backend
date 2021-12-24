package ru.cifrak.telecomit.backend.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.TypeLocation;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationForTable;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationForReference;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationParent;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {
    public static final List<String> PARENT_LOCATION_TYPES
            = Arrays.stream(TypeLocation.values())
            .filter(typeLocation -> typeLocation != TypeLocation.REGION && typeLocation.isCanBeParent())
            .map(TypeLocation::getShorted)
            .collect(Collectors.toList());
/*
    public static final List<String> PARENT_LOCATION_TYPES = new ArrayList<String>() {{
        add("го");
        add("гп");
        add("мо");
        add("р-н");
    }};
*/

    public static final List<String> NOT_PARENT_LOCATION_TYPES
            = Arrays.stream(TypeLocation.values())
            .filter(typeLocation -> !typeLocation.isCanBeParent())
            .map(TypeLocation::getShorted)
            .collect(Collectors.toList());
/*
    public static final List<String> NOT_PARENT_LOCATION_TYPES = new ArrayList<String>() {{
        add("г");
        add("пгт");
        add("д");
        add("с");
        add("п");
    }};
*/

    /**
     * Added .isTrue() and .isFalse(), because .asBoolean() returns ConstantImpl, that are not casts to Predicate.
     */
    private static final BooleanExpression TRUE_EXPRESSION = Expressions.asBoolean(true).isTrue();
    private static final BooleanExpression FALSE_EXPRESSION = Expressions.asBoolean(true).isFalse();

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
        JPAQuery<LocationParent> jQ = new JPAQuery<LocationParent>(entityManager)
                .from(parent)
                .where(parent.level.eq(1));
        List<LocationParent> orderedParents = jQ.fetch();
        orderedParents.sort(((Comparator<LocationParent>) (l1, l2) -> {
            int orderValueL1 =
                    l1.getType().equalsIgnoreCase("г") ? 1 :
                            l1.getType().equalsIgnoreCase("р-н") || l1.getType().equalsIgnoreCase("мо") ? 3 : 2;
            int orderValueL2 =
                    l2.getType().equalsIgnoreCase("г") ? 1 :
                            l2.getType().equalsIgnoreCase("р-н") || l2.getType().equalsIgnoreCase("мо") ? 3 : 2;
            return orderValueL1 - orderValueL2;
        }).thenComparing(LocationParent::getName));
        return orderedParents;

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
            "type_mobiles",
            "feature_edit_full"
    }, allEntries = true)
    public void refreshCache() {
        lastRefreshDate = new Date();
    }

    public Date getLastRefreshDate() {
        return lastRefreshDate;
    }

    public Predicate getPredicateForLocationForReference(
            Boolean canBeParent,
            List<Integer> locationIds,
            List<Integer> parentIds,
            List<String> locationNames,
            LogicalCondition logicalCondition) {
        QLocationForReference locationFR = QLocationForReference.locationForReference;
        BooleanExpression parentPredicate = getParentPredicate(locationFR, parentIds);
        BooleanExpression locationPredicate = getLocationPredicate(locationFR, locationIds);
        BooleanExpression locationNamesPredicate = getLocationNamesPredicate(locationFR, locationNames);
        BooleanExpression predicate = getcanBeParentPredicate(locationFR, canBeParent);
        if (logicalCondition == LogicalCondition.OR) {
            predicate = predicate.and(
                    (locationPredicate != null ? locationPredicate : FALSE_EXPRESSION)
                            .or(parentPredicate != null ? parentPredicate : FALSE_EXPRESSION)
                            .or(locationNamesPredicate != null ? locationNamesPredicate : FALSE_EXPRESSION));
        } else {
            predicate = predicate.and(
                    (locationPredicate != null ? locationPredicate : TRUE_EXPRESSION)
                            .and(parentPredicate != null ? parentPredicate : TRUE_EXPRESSION)
                            .and(locationNamesPredicate != null ? locationNamesPredicate : TRUE_EXPRESSION));
        }
        return predicate;
    }

    private BooleanExpression getcanBeParentPredicate(QLocationForReference locationFR, Boolean canBeParent) {
        return canBeParent == null || !canBeParent ?
                locationFR.type.in(NOT_PARENT_LOCATION_TYPES)
                : locationFR.type.in(PARENT_LOCATION_TYPES);
    }

    private BooleanExpression getParentPredicate(QLocationForReference locationFR, List<Integer> parentIds) {
        return parentIds != null ?
                locationFR.locationParent.id.in(parentIds)
                : null;
    }

    private BooleanExpression getLocationPredicate(QLocationForReference locationFR, List<Integer> locationIds) {
        return locationIds != null ?
                locationFR.id.in(locationIds)
                : null;
    }

    private BooleanExpression getLocationNamesPredicate(QLocationForReference locationFR, List<String> locationNames) {
        return locationNames != null && locationNames.size() > 0 && locationNamesNotNullCheck(locationNames) ?
                locationFR.name.toLowerCase().in(
                        locationNames.stream().map(String::toLowerCase).collect(Collectors.toList()))
                : null;
    }

    private boolean locationNamesNotNullCheck(List<String> locationNames) {
        boolean result = true;
        if (locationNames != null && locationNames.size() > 0) {
            for (String name : locationNames) {
                if (name == null) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
