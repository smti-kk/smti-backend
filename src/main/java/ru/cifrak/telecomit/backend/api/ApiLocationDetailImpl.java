package ru.cifrak.telecomit.backend.api;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.service.DetailLocationFilterBuilder;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.locationsummary.DetailLocation;
import ru.cifrak.telecomit.backend.repository.DSLDetailLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPointsTest;

import javax.annotation.Nullable;
import java.util.List;

@RestController
public class ApiLocationDetailImpl implements ApiLocationDetail {
    private final DSLDetailLocation dslDetailLocation;

    @Autowired
    private RepositoryAccessPointsTest repositoryAccessPointsTest;

    public ApiLocationDetailImpl(DSLDetailLocation dslDetailLocation) {
        this.dslDetailLocation = dslDetailLocation;
    }

    @Override
    public Page<DetailLocation> getList(Pageable pageable,
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
        return dslDetailLocation.findAll(expression, pageable);
    }

    @GetMapping("/test")
    public List<Location> test() {
        return repositoryAccessPointsTest.findAll();
    }
}
