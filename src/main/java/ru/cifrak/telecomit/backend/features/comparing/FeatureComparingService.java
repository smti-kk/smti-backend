package ru.cifrak.telecomit.backend.features.comparing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeatureComparingService {
    Page<LocationFC> locations(
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
    );
}
