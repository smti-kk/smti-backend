package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.*;
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

    private final RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram;

    private final LocationService locationService;

    public ApesSaveService(
            RepositoryLocation repositoryLocation,
            RepositoryAccessPoints repositoryAccessPoints,
            RepositoryOrganization repositoryOrganization,
            RepositoryInternetAccessType repositoryInternetAccessType,
            RepositorySmoType repositorySmoType,
            RepositoryOrganizationType repositoryOrganizationType,
            RepositoryGovernmentDevelopmentProgram repositoryGovernmentDevelopmentProgram,
            LocationService locationService) {
        this.repositoryLocation = repositoryLocation;
        this.repositoryAccessPoints = repositoryAccessPoints;
        this.repositoryOrganization = repositoryOrganization;
        this.repositoryInternetAccessType = repositoryInternetAccessType;
        this.repositorySmoType = repositorySmoType;
        this.repositoryOrganizationType = repositoryOrganizationType;
        this.repositoryGovernmentDevelopmentProgram = repositoryGovernmentDevelopmentProgram;
        this.locationService = locationService;
    }

    public void save(List<ApFromExcelDTO> TcesDTO) {
        for (ApFromExcelDTO tcDTO : TcesDTO) {
            List<AccessPoint> apes = repositoryAccessPoints.findByPointAndOrganization(
                    createPoint(tcDTO.getLongitude(), tcDTO.getLatitude()),
                    getOrganization(tcDTO));
            if (apes.size() > 0) {
                if (getTypeAPInString(apes.get(0)).equals(tcDTO.getTypeAccessPoint())) {
                    // TODO: Transaction.
                    apes.get(0).setAddress(tcDTO.getAddress());
                    apes.get(0).setContractor(tcDTO.getContractor());
                    apes.get(0).setInternetAccess(repositoryInternetAccessType.findByName(tcDTO.getTypeInternetAccess()));
                    apes.get(0).setDeclaredSpeed(tcDTO.getDeclaredSpeed());
                    apes.get(0).setGovernmentDevelopmentProgram(repositoryGovernmentDevelopmentProgram.findByAcronym(tcDTO.getProgram()));
                    repositoryAccessPoints.save(apes.get(0));
                } else {
                    AccessPoint ap;
                    switch (tcDTO.getTypeAccessPoint()) {
                        case ("ЕСПД"):
                            ap = new ApESPD();
                            break;
                        case ("РСЗО"):
                            ap = new ApRSMO();
                            break;
                        case ("СЗО"):
                            ap = new ApSMO();
                            break;
                        case ("ЕМСПД"):
                            ap = new ApEMSPD();
                            break;
                        default:
                            ap = new ApContract();
                            break;
                    }
                    ap.setPoint(createPoint(tcDTO.getLongitude(), tcDTO.getLatitude()));
                    ap.setOrganization(getOrganization(tcDTO));
                    ap.setAddress(tcDTO.getAddress());
                    ap.setContractor(tcDTO.getContractor());
                    ap.setGovernmentDevelopmentProgram(repositoryGovernmentDevelopmentProgram.findByAcronym(tcDTO.getProgram()));
                    ap.setInternetAccess(repositoryInternetAccessType.findByName(tcDTO.getTypeInternetAccess()));
                    ap.setDeclaredSpeed(tcDTO.getDeclaredSpeed());
                    ap.setVisible(true);
                    ap.setMaxAmount(0);
                    ap.setDeleted(false);
                    // TODO: Transaction.
                    repositoryAccessPoints.save(ap);
                }
            }
            locationService.refreshCache();
        }
    }

    private String getTypeAPInString(AccessPoint ap) {
        String type;
        if (ap instanceof ApESPD) {
            type = "ЕСПД";
        } else if (ap instanceof ApRSMO) {
            type = "РСЗО";
        } else if (ap instanceof ApSMO) {
            type = "СЗО";
        } else if (ap instanceof ApEMSPD) {
            type = "ЕМСПД";
        } else {
            type = "Контракт";
        }
        return type;
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
