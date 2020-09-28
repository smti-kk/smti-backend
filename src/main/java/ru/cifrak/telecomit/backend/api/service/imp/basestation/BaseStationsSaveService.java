package ru.cifrak.telecomit.backend.api.service.imp.basestation;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.base.station.entity.BaseStation;
import ru.cifrak.telecomit.backend.base.station.repository.BaseStationsRepository;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.LocationService;

import java.time.LocalDate;
import java.util.*;

@Service
public class BaseStationsSaveService {

    private final RepositoryLocation repositoryLocation;

    private final RepositoryAccessPoints repositoryAccessPoints;

    private final RepositoryOrganization repositoryOrganization;

    private final RepositoryInternetAccessType repositoryInternetAccessType;

    private final RepositorySmoType repositorySmoType;

    private final RepositoryOrganizationType repositoryOrganizationType;

    private final LocationService locationService;

    private final BaseStationsRepository baseStationsRepository;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryMobileType repositoryMobileType;

    public BaseStationsSaveService(
            RepositoryLocation repositoryLocation,
            RepositoryAccessPoints repositoryAccessPoints,
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType,
            LocationService locationService,
            BaseStationsRepository baseStationsRepository,
            RepositoryOperator repositoryOperator,
            RepositoryMobileType repositoryMobileType) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryAccessPoints = repositoryAccessPoints;
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
        this.locationService = locationService;
        this.baseStationsRepository = baseStationsRepository;
        this.repositoryOperator = repositoryOperator;
        this.repositoryMobileType = repositoryMobileType;
    }

    public void save(List<BaseStationFromExcelDTO> TcesDTO) {
        for (BaseStationFromExcelDTO tcDTO : TcesDTO) {
            List<BaseStation> bses = baseStationsRepository.findByPointAndOperatorAndMobileType(
                    createPoint(tcDTO.getLongitude(), tcDTO.getLatitude()),
                    repositoryOperator.findByName(tcDTO.getOperator()),
                    repositoryMobileType.findByName(tcDTO.getMobileType()));
            Date actionDate = new Date(
                    Integer.parseInt(tcDTO.getActionDate().substring(6, 10)),
                    Integer.parseInt(tcDTO.getActionDate().substring(3, 5)) - 1,
                    Integer.parseInt(tcDTO.getActionDate().substring(0, 2))
            );
            if (bses.size() > 0) {
                // TODO: Transaction.
                bses.get(0).setAddress(tcDTO.getAddress());
                bses.get(0).setCoverageRadius(Double.parseDouble(tcDTO.getCoverageRadius()));
                bses.get(0).setPropHeight(Double.parseDouble(tcDTO.getPropHeight()));
                bses.get(0).setActionDate(actionDate);
                baseStationsRepository.save(bses.get(0));
            } else {
                BaseStation bs = new BaseStation();
                bs.setAddress(tcDTO.getAddress());
                bs.setPoint(createPoint(tcDTO.getLongitude(), tcDTO.getLatitude()));
                bs.setOperator(repositoryOperator.findByName(tcDTO.getOperator()));
                bs.setMobileType(repositoryMobileType.findByName(tcDTO.getMobileType()));
                bs.setCoverageRadius(Double.parseDouble(tcDTO.getCoverageRadius()));
                bs.setPropHeight(Double.parseDouble(tcDTO.getPropHeight()));
                bs.setActionDate(actionDate);

                // TODO: Transaction.
                baseStationsRepository.save(bs);
            }
            locationService.refreshCache();
        }
    }

    private Point createPoint(String x, String y) {
        Point p = new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(x), Double.parseDouble(y)));
        p.setSRID(4326);
        return p;
    }
}
