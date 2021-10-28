package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.TcType;

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
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAny,
            TcType type,
            LogicalCondition logicalCondition,
            String... locationNames) {
        FCFiltersPredicate predicate = new FCFiltersPredicate(
                parentIds,
                operators,
                connectionTypes,
                govProgram,
                govProgramYear,
                hasAny,
                type,
                logicalCondition,
                locationNames);
        return locationRepository.findAll(predicate.booleanExpression(), pageable);
    }

    @Override
    public List<LocationFC> locations(
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAny,
            TcType type,
            LogicalCondition logicalCondition,
            String... locationNames) {
        FCFiltersPredicate predicate = new FCFiltersPredicate(
                parentIds,
                operators,
                connectionTypes,
                govProgram,
                govProgramYear,
                hasAny,
                type,
                logicalCondition,
                locationNames);
        List<LocationFC> result = new ArrayList<>();
        locationRepository.findAll(predicate.booleanExpression()).forEach(result::add);
        return result;
    }
}
