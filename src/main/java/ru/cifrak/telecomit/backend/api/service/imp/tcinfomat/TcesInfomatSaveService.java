package ru.cifrak.telecomit.backend.api.service.imp.tcinfomat;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTcForImport;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTcForImport;

import java.util.List;
import java.util.UUID;

@Service
public class TcesInfomatSaveService {

    private final RepositoryWritableTcForImport RepositoryWritableTcForImport;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    public TcesInfomatSaveService(
            RepositoryWritableTcForImport RepositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator) {
        this.RepositoryWritableTcForImport = RepositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
    }

    public void saveTces(List<TcInfomatFromExcelDTO> TcesDTO) {
        for (TcInfomatFromExcelDTO tcDTO : TcesDTO){
            Integer i1 = repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId();
            Integer i2 = repositoryOperator.findByName(tcDTO.getOperator()).getId();
            List<WritableTcForImport> tcesByLocOpT = RepositoryWritableTcForImport.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    "INFOMAT"
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setQuantity(Integer.parseInt(tcDTO.getInfomats()));
                tcesByLocOpT.get(0).setState(TcState.ACTIVE);
                // TODO: Transaction.
                RepositoryWritableTcForImport.save(tcesByLocOpT.get(0));
            } else {
                WritableTcForImport tcByLocOpT = new WritableTcForImport();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setQuantity(Integer.parseInt(tcDTO.getInfomats()));
                tcByLocOpT.setType("INFOMAT");
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                RepositoryWritableTcForImport.save(tcByLocOpT);
            }
        }
    }
}
