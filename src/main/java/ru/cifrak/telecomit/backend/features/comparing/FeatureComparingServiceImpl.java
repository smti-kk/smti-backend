package ru.cifrak.telecomit.backend.features.comparing;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeatureComparingServiceImpl implements FeatureComparingService {

    private final LocationRepository locationRepository;

    public FeatureComparingServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Page<LocationFC> locations(
            Pageable pageable,
            List<Integer> parentIds,
            String locationName,
            List<Integer> internetOperators,
            List<Integer> mobileOperators,
            List<Integer> connectionTypes,
            List<Integer> mobileTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAnyInternet,
            Integer hasAnyMobile
    ) {
        BooleanExpression predicate = new FCFiltersPredicate(
                parentIds,
                locationName,
                internetOperators,
                mobileOperators,
                connectionTypes,
                mobileTypes,
                govProgram,
                govProgramYear,
                hasAnyInternet,
                hasAnyMobile
        ).booleanExpression();
        return locationRepository.findAll(predicate, pageable);
    }

    @Override
    public List<LocationFC> locations(List<Integer> parentIds,
                                      String locationName,
                                      List<Integer> internetOperators,
                                      List<Integer> mobileOperators,
                                      List<Integer> connectionTypes,
                                      List<Integer> mobileTypes,
                                      Integer govProgram,
                                      Integer govProgramYear,
                                      Integer hasAnyInternet,
                                      Integer hasAnyMobile) {
        BooleanExpression predicate = new FCFiltersPredicate(
                parentIds,
                locationName,
                internetOperators,
                mobileOperators,
                connectionTypes,
                mobileTypes,
                govProgram,
                govProgramYear,
                hasAnyInternet,
                hasAnyMobile
        ).booleanExpression();
        List<LocationFC> result = new ArrayList<>();
        locationRepository
                .findAll(predicate)
                .forEach(result::add);
        return result;
    }
}
