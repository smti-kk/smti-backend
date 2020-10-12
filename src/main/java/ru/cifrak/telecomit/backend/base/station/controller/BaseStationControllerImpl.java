package ru.cifrak.telecomit.backend.base.station.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.base.station.BaseStationService;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.Date;
import java.util.List;

@RestController
public class BaseStationControllerImpl implements BaseStationController {

    private final BaseStationService baseStationService;

    public BaseStationControllerImpl(BaseStationService baseStationService) {
        this.baseStationService = baseStationService;
    }

    @Override
    public Page<BaseStation> baseStations(Pageable pageable,
                                          List<Integer> operatorIds,
                                          List<Integer> typeMobiles,
                                          Double coverageRadiusLeftBorder,
                                          Double coverageRadiusRightBorder,
                                          Double propHeightLeftBorder,
                                          Double propHeightRightBorder,
                                          Date actionDateFrom,
                                          Date actionDateTo,
                                          String address) {
        return baseStationService.baseStations(
                pageable,
                operatorIds,
                typeMobiles,
                coverageRadiusLeftBorder,
                coverageRadiusRightBorder,
                propHeightLeftBorder,
                propHeightRightBorder,
                actionDateFrom,
                actionDateTo,
                address
        );
    }

    @Override
    public List<BaseStation> baseStations() {
        return baseStationService.baseStations();
    }

    @Override
    public BaseStation baseStation(Integer baseStationId) throws NotFoundException {
        return baseStationService.baseStation(baseStationId);
    }

    @Override
    public void remove(Integer baseStationId) {
        baseStationService.remove(baseStationId);
    }

    @Override
    public BaseStation save(BaseStation baseStation) {
        return baseStationService.save(baseStation);
    }

    @Override
    public BaseStation update(BaseStation baseStation) {
        return baseStationService.save(baseStation);
    }
}
