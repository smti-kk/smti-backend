package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.repository.*;

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

    public ApesSaveService(
            RepositoryLocation repositoryLocation,
            RepositoryAccessPoints repositoryAccessPoints,
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryAccessPoints = repositoryAccessPoints;
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
    }

    public void save(List<ApFromExcelDTO> TcesDTO) {
        for (ApFromExcelDTO tcDTO : TcesDTO){
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
                ap.setContractor(tcDTO.getContractor());
                ap.setInternetAccess(repositoryInternetAccessType.findByName(tcDTO.getTypeInternetAccess()));
                ap.setDeclaredSpeed(tcDTO.getDeclaredSpeed());
                // TODO: Transaction.
                repositoryAccessPoints.save(ap);
            }
        }
    }

    private Organization getOrganization(ApFromExcelDTO ap) {
        Organization organization = repositoryOrganization.findByFias(UUID.fromString(ap.getFias()));
        if (organization == null) {
            organization.setFias(UUID.fromString(ap.getFias()));
            Location location = repositoryLocation.findByFias(UUID.fromString(ap.getFiasLocation()));
            if (location != null) {
                organization.setLocation(location);
            }
            organization.setName(ap.getName());
            organization.setAddress(ap.getAddress());
            organization.setSmo(repositorySmoType.findByName(ap.getSmo()));
            organization.setType(repositoryOrganizationType.findByName(ap.getType()));
        }
        return organization;
    }

    private Point createPoint(String x, String y) {
        Point p = new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(x), Double.parseDouble(y)));
        p.setSRID(4326);
        return p;
    }
}
