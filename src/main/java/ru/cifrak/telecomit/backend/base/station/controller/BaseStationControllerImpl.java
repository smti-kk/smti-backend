package ru.cifrak.telecomit.backend.base.station.controller;

import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.base.station.repository.BaseStationsRepository;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;

import java.util.List;

@RestController
public class BaseStationControllerImpl implements BaseStationController {

    private final BaseStationsRepository baseStationsRepository;

    public BaseStationControllerImpl(BaseStationsRepository baseStationsRepository) {
        this.baseStationsRepository = baseStationsRepository;
    }

    public List<BaseStation> baseStations() {
        return baseStationsRepository.findAll();
    }

    public BaseStation baseStation(Integer baseStationId) throws NotFoundException {
        return baseStationsRepository
                .findById(baseStationId)
                .orElseThrow(NotFoundException::new);
    }

    public void remove(Integer baseStationId) {
        baseStationsRepository.deleteById(baseStationId);
    }

    public BaseStation save(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }

    @Override
    public BaseStation update(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }
}
