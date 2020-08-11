package ru.cifrak.telecomit.backend.api.service.imp.tcats;

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
public class TcesAtsSaveService {

    private final RepositoryWritableTc repositoryWritableTc;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    public TcesAtsSaveService(
            RepositoryWritableTc repositoryWritableTc,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator) {
        this.repositoryWritableTc = repositoryWritableTc;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
    }

    public void saveTces(List<TcAtsFromExcelDTO> TcesDTO) {
        for (TcAtsFromExcelDTO tcDTO : TcesDTO){
            List<WritableTc> tcesByLocOpT = repositoryWritableTc.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcAts.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setPayphones(Integer.parseInt(tcDTO.getPayphones()));
                // TODO: Transaction.
                repositoryWritableTc.save(tcesByLocOpT.get(0));
            } else {
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setPayphones(Integer.parseInt(tcDTO.getPayphones()));
                tcByLocOpT.setType(TcAts.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                // TODO: Transaction.
                repositoryWritableTc.save(tcByLocOpT);
            }
        }
    }
}
