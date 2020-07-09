package ru.cifrak.telecomit.backend.api;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;
import ru.cifrak.telecomit.backend.entities.locationsummary.QDetailLocation;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.utils.LogicalAndOrLogicalOrFilter;
import ru.cifrak.telecomit.backend.utils.LogicalAndOrLogicalOrFilterType;

import javax.annotation.Nullable;
import java.util.List;

@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    private final DSLDetailLocation dslDetailLocation;

    public ApiLocationDetailImpl(DSLDetailLocation dslDetailLocation) {
        this.dslDetailLocation = dslDetailLocation;
    }

    @Override
    public Page<DetailLocation> getList(Pageable pageable,
                                        @Nullable List<Integer> mobileTypes,
                                        @Nullable List<Integer> internetTypes,
                                        Boolean isLogicalOr) {
        LogicalAndOrLogicalOrFilterType filterType;
        if (isLogicalOr) {
            filterType = LogicalAndOrLogicalOrFilterType.OR;
        } else {
            filterType = LogicalAndOrLogicalOrFilterType.AND;
        }
        LogicalAndOrLogicalOrFilter eq = new LogicalAndOrLogicalOrFilter(filterType);
        if (mobileTypes != null) {
            for (Integer typeMobile : mobileTypes) {
                eq = eq.with(QDetailLocation.detailLocation.shortTechnicalCapability.any().typeMobile.id.eq(typeMobile));
            }
        }
        if (internetTypes != null) {
            for (Integer trunkChannel : internetTypes) {
                eq = eq.with(QDetailLocation.detailLocation.shortTechnicalCapability.any().trunkChannel.id.eq(trunkChannel));
            }
        }
        QDetailLocation.detailLocation.shortTechnicalCapability.any().operatorId.eq()
        BooleanExpression expression = eq.build();
        if (expression == null) {
            return dslDetailLocation.findAll(pageable);
        } else {
            return dslDetailLocation.findAll(expression, pageable);
        }
    }
}
