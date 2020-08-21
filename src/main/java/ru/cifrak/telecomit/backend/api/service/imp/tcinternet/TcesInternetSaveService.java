package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.TcInternet;
import ru.cifrak.telecomit.backend.entities.TcPost;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import javax.persistence.DiscriminatorValue;
import java.util.List;
import java.util.UUID;

@Service
public class TcesInternetSaveService {

    private final RepositoryWritableTc repositoryWritableTc;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    public TcesInternetSaveService(
            RepositoryWritableTc repositoryWritableTc,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel) {
        this.repositoryWritableTc = repositoryWritableTc;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
    }

    public void saveTcesInternet(List<TcInternetFromExcelDTO> TcesInternetDTO) {
        for (TcInternetFromExcelDTO tcDTO : TcesInternetDTO){
            List<WritableTc> tcesByLocOpT = repositoryWritableTc.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcInternet.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getChannel()).getId());
                tcesByLocOpT.get(0).setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTc.save(tcesByLocOpT.get(0));
            } else {
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getChannel()).getId());
                tcByLocOpT.setType(TcInternet.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTc.save(tcByLocOpT);
            }
        }
    }
}
