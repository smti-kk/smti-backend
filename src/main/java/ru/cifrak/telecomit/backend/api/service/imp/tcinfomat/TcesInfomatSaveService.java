package ru.cifrak.telecomit.backend.api.service.imp.tcinfomat;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.TcAts;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import javax.persistence.DiscriminatorValue;
import java.util.List;
import java.util.UUID;

@Service
public class TcesInfomatSaveService {

    private final RepositoryWritableTc repositoryWritableTc;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    public TcesInfomatSaveService(
            RepositoryWritableTc repositoryWritableTc,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator) {
        this.repositoryWritableTc = repositoryWritableTc;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
    }

    public void saveTces(List<TcInfomatFromExcelDTO> TcesDTO) {
        for (TcInfomatFromExcelDTO tcDTO : TcesDTO){
            Integer i1 = repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId();
            Integer i2 = repositoryOperator.findByName(tcDTO.getOperator()).getId();
            List<WritableTc> tcesByLocOpT = repositoryWritableTc.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    "INFOMAT"
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setPayphones(Integer.parseInt(tcDTO.getInfomats()));
                // TODO: Transaction.
                repositoryWritableTc.save(tcesByLocOpT.get(0));
            } else {
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setPayphones(Integer.parseInt(tcDTO.getInfomats()));
                tcByLocOpT.setType("INFOMAT");
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                // TODO: Transaction.
                repositoryWritableTc.save(tcByLocOpT);
            }
        }
    }
}
