package ru.cifrak.telecomit.backend.api.service.imp.tcmobile;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import javax.persistence.DiscriminatorValue;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class TcesMobileSaveService {

    private final RepositoryWritableTcForImport repositoryWritableTcForImport;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryMobileType repositoryMobileType;

    private final LocationService locationService;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;
    private final RepositoryFeatureEdits repositoryFeatureEdits;
    private final RepositoryWritableTc rWritableTc;


    public TcesMobileSaveService(
            RepositoryWritableTcForImport repositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            RepositoryMobileType repositoryMobileType,
            LocationService locationService, RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests, ServiceWritableTc serviceWritableTc, RepositoryFeatureEdits repositoryFeatureEdits, RepositoryWritableTc rWritableTc) {
        this.repositoryWritableTcForImport = repositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.repositoryMobileType = repositoryMobileType;
        this.locationService = locationService;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableTc = serviceWritableTc;
        this.repositoryFeatureEdits = repositoryFeatureEdits;
        this.rWritableTc = rWritableTc;
    }

    public void save(List<TcMobileFromExcelDTO> TcesMobileDTO, User user) {
        for (TcMobileFromExcelDTO tcDTO : TcesMobileDTO){
            List<WritableTc> tcesByLocOpT = repositoryWritableTcForImport.findByLocationIdAndOperatorIdAndTypeAndState(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcMobile.class.getAnnotation(DiscriminatorValue.class).value(),
                    TcState.ACTIVE
            );
            if (tcesByLocOpT.size() > 0) {
                WritableTc clonedTc = tcesByLocOpT.get(0).cloneWithNullId();
                clonedTc.setTypeMobile(repositoryMobileType.findByName(tcDTO.getType()).getId());
                clonedTc = rWritableTc.save(clonedTc);
                FeatureEdit featureEdit = new FeatureEdit(tcesByLocOpT.get(0), clonedTc);
                featureEdit.setAction(tcDTO.getActivity().equals("НЕТ") ? FeatureEditAction.DELETE
                        : featureEdit.getAction());
                featureEdit = repositoryFeatureEdits.save(featureEdit);
                LocationFeaturesEditingRequest importRequest = new LocationFeaturesEditingRequest(
                        tcesByLocOpT.get(0).getLocationId(),
                        "",
                        user,
                        ChangeSource.IMPORT,
                        Collections.singleton(featureEdit)
                );
                importRequest.accept(serviceWritableTc);
                repositoryLocationFeaturesRequests.save(importRequest);
            } else {
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setTypeMobile(repositoryMobileType.findByName(tcDTO.getType()).getId());
                tcByLocOpT.setType(TcMobile.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.GOOD);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                // start journal
                rWritableTc.save(tcByLocOpT);
                FeatureEdit featureEdit = new FeatureEdit(tcByLocOpT, FeatureEditAction.CREATE);
                repositoryFeatureEdits.save(featureEdit);
                LocationFeaturesEditingRequest importRequest = new LocationFeaturesEditingRequest(
                        tcByLocOpT.getLocationId(),
                        "",
                        user,
                        ChangeSource.IMPORT,
                        Collections.singleton(featureEdit)
                );
                repositoryLocationFeaturesRequests.save(importRequest);
                importRequest.accept(serviceWritableTc);
                // end journal
            }
            locationService.refreshCache();
        }
    }
}
