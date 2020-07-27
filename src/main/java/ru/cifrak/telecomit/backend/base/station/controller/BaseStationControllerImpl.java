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

    public BaseStation baseStation(BaseStation baseStation) throws NotFoundException {
        if (baseStation == null) {
            throw new NotFoundException();
        }
        return baseStation;
    }

    public void remove(Integer baseStationId) {
        baseStationsRepository.deleteById(baseStationId);
    }

    public void save(BaseStation baseStation) {
        baseStationsRepository.save(baseStation);
    }
}
