package ru.cifrak.telecomit.backend.features.comparing;

import com.querydsl.core.types.OrderSpecifier;

public enum LocationOrdering {
    LOCATION_ASC(QLocationFC.locationFC.name.asc()),
    LOCATION_DSC(QLocationFC.locationFC.name.desc()),
    AREA_ASC(QLocationFC.locationFC.locationParent.name.asc()),
    AREA_DSC(QLocationFC.locationFC.locationParent.name.asc()),
    POPULATION_ASC(QLocationFC.locationFC.population.asc()),
    POPULATION_DSC(QLocationFC.locationFC.population.desc());

    private final OrderSpecifier<?> orderSpecifier;

    LocationOrdering(OrderSpecifier<?> orderSpecifier) {
        this.orderSpecifier = orderSpecifier;
    }

    public OrderSpecifier<?> getOrderSpecifier() {
        return orderSpecifier;
    }
}
