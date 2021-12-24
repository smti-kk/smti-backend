package ru.cifrak.telecomit.backend.base.station;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.entities.LogicalCondition;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.Date;
import java.util.List;

public interface BaseStationService {
    Page<BaseStation> baseStations(Pageable pageable,
                                   @Nullable List<Integer> operatorIds,
                                   @Nullable List<Integer> typeMobiles,
                                   @Nullable Double coverageRadiusLeftBorder,
                                   @Nullable Double coverageRadiusRightBorder,
                                   @Nullable Double propHeightLeftBorder,
                                   @Nullable Double propHeightRightBorder,
                                   @Nullable Date actionDateFrom,
                                   @Nullable Date actionDateTo,
                                   @Nullable String address,
                                   @Nullable LogicalCondition logicalCondition);

    List<BaseStation> baseStations();

    BaseStation baseStation(Integer baseStationId) throws NotFoundException;

    void remove(Integer baseStationId);

    BaseStation save(BaseStation baseStation);

    BaseStation update(BaseStation baseStation);
}
