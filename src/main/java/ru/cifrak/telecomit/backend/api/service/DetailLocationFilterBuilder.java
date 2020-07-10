package ru.cifrak.telecomit.backend.api.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import ru.cifrak.telecomit.backend.entities.locationsummary.QDetailLocation;
import ru.cifrak.telecomit.backend.entities.map.QShortTechnicalCapability;
import ru.cifrak.telecomit.backend.utils.LogicalAndOrLogicalOrFilter;
import ru.cifrak.telecomit.backend.utils.LogicalAndOrLogicalOrFilterType;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class DetailLocationFilterBuilder {
    private LogicalAndOrLogicalOrFilter filter;

    public DetailLocationFilterBuilder() {
        filter = new LogicalAndOrLogicalOrFilter(LogicalAndOrLogicalOrFilterType.AND);
    }

    public DetailLocationFilterBuilder logicalOperation(Boolean isLogicalOr) {
        if (isLogicalOr) {
            filter.setType(LogicalAndOrLogicalOrFilterType.OR);
        } else {
            filter.setType(LogicalAndOrLogicalOrFilterType.AND);
        }
        return this;
    }

    public DetailLocationFilterBuilder mobileTypes(@Nullable List<Integer> mobileTypes) {
        if (mobileTypes != null) {
            for (Integer typeMobile : mobileTypes) {
                filter = filter.with(
                        QDetailLocation.detailLocation
                                .shortTechnicalCapability
                                .any()
                                .typeMobile.id.eq(typeMobile)
                );
            }
        }
        return this;
    }

    public DetailLocationFilterBuilder internetTypes(@Nullable List<Integer> internetTypes) {
        if (internetTypes != null) {
            for (Integer trunkChannel : internetTypes) {
                filter = filter.with(
                        QDetailLocation.detailLocation
                                .shortTechnicalCapability
                                .any()
                                .trunkChannel.id.eq(trunkChannel)
                );
            }
        }
        return this;
    }

    public DetailLocationFilterBuilder location(@Nullable String location) {
        if (location != null) {
            filter = filter.with(
                    QDetailLocation.detailLocation
                            .name
                            .lower()
                            .like("%" + location.toLowerCase() + "%")
            );
        }
        return this;
    }

    public DetailLocationFilterBuilder parent(@Nullable String parent) {
        if (parent != null) {
            filter = filter.with(
                    QDetailLocation.detailLocation
                            .locationParent
                            .name
                            .lower()
                            .like("%" + parent.toLowerCase() + "%")
            );
        }
        return this;
    }

    public DetailLocationFilterBuilder internetOperators(@Nullable List<Integer> operators) {
        QDetailLocation detailLocation = QDetailLocation.detailLocation;
        QShortTechnicalCapability technicalCapability = QShortTechnicalCapability.shortTechnicalCapability;
        if (operators != null) {
            for (Integer operatorId : operators) {
                BooleanExpression predicate = JPAExpressions
                        .selectOne()
                        .from(technicalCapability)
                        .where(
                                technicalCapability.type.eq("INET")
                                        .and(technicalCapability.operatorId.eq(operatorId))
                                        .and(detailLocation.id.eq(technicalCapability.locationId))
                        )
                        .exists();
                filter = filter.with(predicate);
            }
        }
        return this;
    }

    public DetailLocationFilterBuilder cellularOperators(@Nullable List<Integer> operators) {
        QDetailLocation detailLocation = QDetailLocation.detailLocation;
        QShortTechnicalCapability technicalCapability = QShortTechnicalCapability.shortTechnicalCapability;
        if (operators != null) {
            for (Integer operatorId : operators) {
                BooleanExpression predicate = JPAExpressions
                        .selectOne()
                        .from(technicalCapability)
                        .where(
                                technicalCapability.type.eq("MOBILE")
                                        .and(technicalCapability.operatorId.eq(operatorId))
                                        .and(detailLocation.id.eq(technicalCapability.locationId))
                        )
                        .exists();
                filter = filter.with(predicate);
            }
        }
        return this;
    }

    @NotNull
    public BooleanExpression build() {
        BooleanExpression expression = filter.build();
        BooleanExpression withoutNpRnSSTer = QDetailLocation.detailLocation
                .type.notIn(Arrays.asList("р-н", "край", "с/с", "тер"));
        if (expression != null) {
            return expression.and(withoutNpRnSSTer);
        } else {
            return withoutNpRnSSTer;
        }
    }
}
