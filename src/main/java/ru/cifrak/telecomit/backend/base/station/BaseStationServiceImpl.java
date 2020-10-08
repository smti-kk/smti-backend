package ru.cifrak.telecomit.backend.base.station;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.base.station.entity.QBaseStation;
import ru.cifrak.telecomit.backend.base.station.repository.BaseStationsRepository;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BaseStationServiceImpl implements BaseStationService {

    private final BaseStationsRepository baseStationsRepository;

    public BaseStationServiceImpl(BaseStationsRepository baseStationsRepository) {
        this.baseStationsRepository = baseStationsRepository;
    }

    @Override
    public Page<BaseStation> baseStations(Pageable pageable,
                                          @Nullable List<Integer> operatorIds,
                                          @Nullable List<Integer> typeMobiles,
                                          @Nullable Double coverageRadiusLeftBorder,
                                          @Nullable Double coverageRadiusRightBorder,
                                          @Nullable Double propHeightLeftBorder,
                                          @Nullable Double propHeightRightBorder,
                                          @Nullable Date actionDateFrom,
                                          @Nullable Date actionDateTo,
                                          @Nullable String address) {
        QBaseStation qBaseStation = QBaseStation.baseStation;
        BooleanExpression predicate = qBaseStation.id.isNotNull();
        if (Objects.nonNull(coverageRadiusLeftBorder)) {
            predicate = predicate.and(qBaseStation.coverageRadius.goe(coverageRadiusLeftBorder));
        }
        if (Objects.nonNull(coverageRadiusRightBorder)) {
            predicate = predicate.and(qBaseStation.coverageRadius.loe(coverageRadiusRightBorder));
        }
        if (Objects.nonNull(propHeightLeftBorder)) {
            predicate = predicate.and(qBaseStation.propHeight.goe(propHeightLeftBorder));
        }
        if (Objects.nonNull(propHeightRightBorder)) {
            predicate = predicate.and(qBaseStation.propHeight.loe(propHeightRightBorder));
        }
        if (Objects.nonNull(actionDateFrom)) {
            predicate = predicate.and(qBaseStation.actionDate.after(actionDateFrom));
        }
        if (Objects.nonNull(actionDateTo)) {
            predicate = predicate.and(qBaseStation.actionDate.before(actionDateFrom));
        }
        if (Objects.nonNull(address)) {
            predicate = predicate.and(qBaseStation.address.containsIgnoreCase(address));
        }
        if (Objects.nonNull(operatorIds)) {
            predicate = predicate.and(qBaseStation.operator.id.in(operatorIds));
        }
        if (Objects.nonNull(typeMobiles)) {
            predicate = predicate.and(qBaseStation.mobileType.id.in(typeMobiles));
        }
        return baseStationsRepository.findAll(predicate, pageable);
    }

    @Override
    public List<BaseStation> baseStations() {
        return baseStationsRepository.findAll();
    }

    @Override
    public BaseStation baseStation(Integer baseStationId) throws NotFoundException {
        return baseStationsRepository
                .findById(baseStationId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void remove(Integer baseStationId) {
        baseStationsRepository.deleteById(baseStationId);
    }

    @Override
    public BaseStation save(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }

    @Override
    public BaseStation update(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }
}
