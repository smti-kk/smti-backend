package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.entities.TcType;

import java.util.List;

public interface FeatureComparingService {
    Page<LocationFC> locations(
            Pageable pageable,
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAny,
            TcType tcType,
            LogicalCondition logicalCondition,
            String... locationNames
    );

    List<LocationFC> locations(
            List<Integer> parentIds,
            List<Integer> operators,
            List<Integer> connectionTypes,
            Integer govProgram,
            Integer govProgramYear,
            Integer hasAny,
            TcType tcType,
            LogicalCondition logicalCondition,
            String... locationNames
    );
}
