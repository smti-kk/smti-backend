package ru.cifrak.telecomit.backend.base.station.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
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

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public Page<BaseStation> baseStations(Pageable pageable) {
        return baseStationsRepository.findAll(pageable);
    }

    @Override
    public List<BaseStation> baseStations() {
        return baseStationsRepository.findAll();
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public BaseStation baseStation(Integer baseStationId) throws NotFoundException {
        return baseStationsRepository
                .findById(baseStationId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public void remove(Integer baseStationId) {
        baseStationsRepository.deleteById(baseStationId);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public BaseStation save(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_OPERATOR"})
    public BaseStation update(BaseStation baseStation) {
        return baseStationsRepository.save(baseStation);
    }
}
