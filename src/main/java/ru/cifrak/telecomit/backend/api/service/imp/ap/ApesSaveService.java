package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.LocationService;

import java.util.List;
import java.util.UUID;

@Service
public class ApesSaveService {

    private final RepositoryLocation repositoryLocation;

    private final RepositoryAccessPoints repositoryAccessPoints;

    private final RepositoryOrganization repositoryOrganization;

    private final RepositoryInternetAccessType repositoryInternetAccessType;

    private final RepositorySmoType repositorySmoType;

    private final RepositoryOrganizationType repositoryOrganizationType;

    private final LocationService locationService;

    public ApesSaveService(
            RepositoryLocation repositoryLocation,
            RepositoryAccessPoints repositoryAccessPoints,
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType,
            LocationService locationService) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryAccessPoints = repositoryAccessPoints;
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
        this.locationService = locationService;
    }

    public void save(List<ApFromExcelDTO> TcesDTO) {
        for (ApFromExcelDTO tcDTO : TcesDTO) {
            List<AccessPoint> apes = repositoryAccessPoints.findByPointAndOrganization(
                    createPoint(tcDTO.getLatitude(), tcDTO.getLongitude()),
                    getOrganization(tcDTO));
            if (apes.size() > 0) {
                // TODO: Transaction.
                apes.get(0).setContractor(tcDTO.getContractor());
                apes.get(0).setInternetAccess(repositoryInternetAccessType.findByName(tcDTO.getTypeInternetAccess()));
                apes.get(0).setDeclaredSpeed(tcDTO.getDeclaredSpeed());
                repositoryAccessPoints.save(apes.get(0));
            } else {
                AccessPoint ap = new AccessPoint();
                ap.setPoint(createPoint(tcDTO.getLatitude(), tcDTO.getLongitude()));
                ap.setOrganization(getOrganization(tcDTO));
                ap.setContractor(tcDTO.getContractor());
                ap.setInternetAccess(repositoryInternetAccessType.findByName(tcDTO.getTypeInternetAccess()));
                ap.setDeclaredSpeed(tcDTO.getDeclaredSpeed());
                ap.setVisible(true);
                ap.setMaxAmount(0);
                ap.setDeleted(false);
                // TODO: Transaction.
                repositoryAccessPoints.save(ap);
            }
            locationService.refreshCache();
        }
    }

    private Organization getOrganization(ApFromExcelDTO ap) {
        Organization organization = repositoryOrganization.findByFias(UUID.fromString(ap.getFias()));
        if (organization == null) {
            organization = new Organization();
            organization.setFias(UUID.fromString(ap.getFias()));
            Location location = repositoryLocation.findByFias(UUID.fromString(ap.getFiasLocation()));
            if (location != null) {
                organization.setLocation(location);
            }
            organization.setName(ap.getName());
            organization.setAddress(ap.getAddress());
            organization.setSmo(repositorySmoType.findByName(ap.getSmo()));
            organization.setType(repositoryOrganizationType.findByName(ap.getType()));
            organization.setAcronym("");
            organization.setInn("");
            organization.setKpp("");
            repositoryOrganization.save(organization);
        }
        return organization;
    }

    private Point createPoint(String x, String y) {
        Point p = new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(x), Double.parseDouble(y)));
        p.setSRID(4326);
        return p;
    }
}
