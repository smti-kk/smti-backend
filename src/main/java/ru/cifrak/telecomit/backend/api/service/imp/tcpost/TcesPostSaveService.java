package ru.cifrak.telecomit.backend.api.service.imp.tcpost;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTcForImport;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTcForImport;
import ru.cifrak.telecomit.backend.service.LocationService;

import javax.persistence.DiscriminatorValue;
import java.util.List;
import java.util.UUID;

@Service
public class TcesPostSaveService {

    private final RepositoryWritableTcForImport repositoryWritableTcForImport;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final LocationService locationService;

    public TcesPostSaveService(
            RepositoryWritableTcForImport repositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            LocationService locationService) {
        this.repositoryWritableTcForImport = repositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.locationService = locationService;
    }

    public void save(List<TcPostFromExcelDTO> TcesDTO) {
        for (TcPostFromExcelDTO tcDTO : TcesDTO){
            List<WritableTcForImport> tcesByLocOpT = repositoryWritableTcForImport.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcPost.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                if (tcDTO.getTypePost().equals(TypePost.UPS.getName())) {
                    tcesByLocOpT.get(0).setTypePost(TypePost.UPS.getId());
                } else if (tcDTO.getTypePost().equals(TypePost.POST.getName())) {
                    tcesByLocOpT.get(0).setTypePost(TypePost.POST.getId());
                }
                tcesByLocOpT.get(0).setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTcForImport.save(tcesByLocOpT.get(0));
            } else {
                WritableTcForImport tcByLocOpT = new WritableTcForImport();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                if (tcDTO.getTypePost().equals(TypePost.UPS.getName())) {
                    tcByLocOpT.setTypePost(TypePost.UPS.getId());
                } else if (tcDTO.getTypePost().equals(TypePost.POST.getName())) {
                    tcByLocOpT.setTypePost(TypePost.POST.getId());
                }
                tcByLocOpT.setType(TcPost.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTcForImport.save(tcByLocOpT);
            }
            locationService.refreshCache();
        }
    }
}
