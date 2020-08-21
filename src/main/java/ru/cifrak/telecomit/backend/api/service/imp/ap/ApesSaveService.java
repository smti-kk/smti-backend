package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.TcPost;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import javax.persistence.DiscriminatorValue;
import java.util.List;
import java.util.UUID;

@Service
public class ApesSaveService {

    private final RepositoryWritableTc repositoryWritableTc;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryAccessPoints repositoryAccessPoints;

    public ApesSaveService(
            RepositoryWritableTc repositoryWritableTc,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryAccessPoints repositoryAccessPoints) {
        this.repositoryWritableTc = repositoryWritableTc;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryAccessPoints = repositoryAccessPoints;
    }

    public void save(List<ApFromExcelDTO> TcesDTO) {
        // TODO: To realization.
        Integer organizationId = 0;
        for (ApFromExcelDTO tcDTO : TcesDTO){
            List<AccessPoint> apesByOrgId = repositoryAccessPoints.getAllByOrganizationId(organizationId);
            if (apesByOrgId.size() > 0) {
                // TODO: To realization.
//                apesByOrgId.get(0).setTypePost(tcDTO.getTypePost());
                // TODO: Transaction.
                repositoryAccessPoints.save(apesByOrgId.get(0));
            } else {
/*
                Entity:
                private Boolean visible;
                private Point point;

                DTO:
                private final String npp;
                private final String latitude;
                private final String longitude;
                private final String organization;
                private final String contractor;
                private final String typeInternetAccess;
                private final String declaredSpeed;
*/
                AccessPoint apByOrgId = new AccessPoint();
                apByOrgId.setPoint(null);
                apByOrgId.setVisible(true);
                // TODO: Transaction.
                repositoryAccessPoints.save(apByOrgId);
            }
        }
    }
}
