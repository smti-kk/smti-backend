package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.TcInternet;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTcForImport;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTcForImport;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.persistence.DiscriminatorValue;
import java.util.List;
import java.util.UUID;

@Service
public class TcesInternetSaveService {

    private final RepositoryWritableTcForImport RepositoryWritableTcForImport;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final LocationService locationService;

    public TcesInternetSaveService(
            RepositoryWritableTcForImport RepositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            LocationService locationService) {
        this.RepositoryWritableTcForImport = RepositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
        this.locationService = locationService;
    }

    public void save(List<TcInternetFromExcelDTO> TcesInternetDTO) {
        for (TcInternetFromExcelDTO tcDTO : TcesInternetDTO){
            List<WritableTcForImport> tcesByLocOpT = RepositoryWritableTcForImport.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcInternet.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getChannel()).getId());
                tcesByLocOpT.get(0).setState(TcState.ACTIVE);
                // TODO: Transaction.
                RepositoryWritableTcForImport.save(tcesByLocOpT.get(0));
            } else {
                WritableTcForImport tcByLocOpT = new WritableTcForImport();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setTrunkChannel(repositoryTypeTruncChannel.findByName(tcDTO.getChannel()).getId());
                tcByLocOpT.setType(TcInternet.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                RepositoryWritableTcForImport.save(tcByLocOpT);
            }
            locationService.refreshCache();
        }
    }
}
