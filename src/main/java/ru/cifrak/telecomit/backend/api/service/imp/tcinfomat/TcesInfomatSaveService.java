package ru.cifrak.telecomit.backend.api.service.imp.tcinfomat;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.TcAts;
import ru.cifrak.telecomit.backend.entities.TcInfomat;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTcForImport;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTcForImport;

import javax.persistence.DiscriminatorValue;
import java.util.List;
import java.util.UUID;

@Service
public class TcesInfomatSaveService {

    private final RepositoryWritableTcForImport repositoryWritableTcForImport;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    public TcesInfomatSaveService(
            RepositoryWritableTcForImport repositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator) {
        this.repositoryWritableTcForImport = repositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
    }

    public void saveTces(List<TcInfomatFromExcelDTO> TcesDTO) {
        for (TcInfomatFromExcelDTO tcDTO : TcesDTO){
            List<WritableTcForImport> tcesByLocOpT = repositoryWritableTcForImport.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcInfomat.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setQuantity(Integer.parseInt(tcDTO.getQuantity()));
                tcesByLocOpT.get(0).setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTcForImport.save(tcesByLocOpT.get(0));
            } else {
                WritableTcForImport tcByLocOpT = new WritableTcForImport();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setQuantity(Integer.parseInt(tcDTO.getQuantity()));
                tcByLocOpT.setType(TcInfomat.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTcForImport.save(tcByLocOpT);
            }
        }
    }
}
