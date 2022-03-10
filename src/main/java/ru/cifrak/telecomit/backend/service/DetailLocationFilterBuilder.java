package ru.cifrak.telecomit.backend.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import ru.cifrak.telecomit.backend.entities.TypeLocation;
import ru.cifrak.telecomit.backend.entities.locationsummary.QLocationForTable;
import ru.cifrak.telecomit.backend.entities.map.QTechnicalCapabilityForLocationTable;
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
                        QLocationForTable.locationForTable
                                .technicalCapabilities
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
                        QLocationForTable.locationForTable
                                .technicalCapabilities
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
                    QLocationForTable.locationForTable
                            .name
                            .lower()
                            .like("%" + location.toLowerCase() + "%")
            );
        }
        return this;
    }

    /**
     * Adds condition for parent location. Must be without TypeLocation.REGION.
     *
     * @param parent name of parent location
     * @return filter with added parent filter
     */
    public DetailLocationFilterBuilder parent(@Nullable String parent) {
        if (parent != null) {
            filter = filter.with(
                    QLocationForTable.locationForTable
                            .locationParent
                            .name
                            .lower()
                            .like("%" + parent.toLowerCase() + "%")
            );
        }
        filter = filter.with(
                QLocationForTable.locationForTable
                        .locationParent
                        .type
                        .lower()
                        .ne(TypeLocation.REGION.getShorted())
        );
        return this;
    }

    public DetailLocationFilterBuilder internetOperators(@Nullable List<Integer> operators) {
        QLocationForTable locationForTable = QLocationForTable.locationForTable;
        QTechnicalCapabilityForLocationTable tc = QTechnicalCapabilityForLocationTable.technicalCapabilityForLocationTable;
        if (operators != null) {
            for (Integer operatorId : operators) {
                BooleanExpression predicate = JPAExpressions
                        .selectOne()
                        .from(tc)
                        .where(
                                tc.type.eq("INET")
                                        .and(tc.operatorId.eq(operatorId))
                                        .and(locationForTable.id.eq(tc.locationId))
                        )
                        .exists();
                filter = filter.with(predicate);
            }
        }
        return this;
    }

    public DetailLocationFilterBuilder cellularOperators(@Nullable List<Integer> operators) {
        QLocationForTable locationForTable = QLocationForTable.locationForTable;
        QTechnicalCapabilityForLocationTable tc = QTechnicalCapabilityForLocationTable.technicalCapabilityForLocationTable;
        if (operators != null) {
            for (Integer operatorId : operators) {
                BooleanExpression predicate = JPAExpressions
                        .selectOne()
                        .from(tc)
                        .where(
                                tc.type.eq("MOBILE")
                                        .and(tc.operatorId.eq(operatorId))
                                        .and(locationForTable.id.eq(tc.locationId))
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
        BooleanExpression withoutNpRnSSTer = QLocationForTable.locationForTable
                .type.notIn(Arrays.asList("р-н", "край", "с/с", "тер", "мо", "го"));
        if (expression != null) {
            return expression.and(withoutNpRnSSTer);
        } else {
            return withoutNpRnSSTer;
        }
    }
}
