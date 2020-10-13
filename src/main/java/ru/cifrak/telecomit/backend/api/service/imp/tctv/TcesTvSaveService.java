package ru.cifrak.telecomit.backend.api.service.imp.tctv;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;

import javax.persistence.DiscriminatorValue;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TcesTvSaveService {

    private final static String ATV = "АТВ";

    private final static String CTV = "ЦТВ";

    private final RepositoryWritableTcForImport repositoryWritableTcForImport;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    private final LocationService locationService;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;
    private final RepositoryFeatureEdits repositoryFeatureEdits;
    private final RepositoryWritableTc rWritableTc;


    public TcesTvSaveService(
            RepositoryWritableTcForImport repositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator,
            LocationService locationService, RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests, ServiceWritableTc serviceWritableTc, RepositoryFeatureEdits repositoryFeatureEdits, RepositoryWritableTc rWritableTc) {
        this.repositoryWritableTcForImport = repositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
        this.locationService = locationService;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableTc = serviceWritableTc;
        this.repositoryFeatureEdits = repositoryFeatureEdits;
        this.rWritableTc = rWritableTc;
    }

    public void save(List<TcTvFromExcelDTO> TcesDTO, User user) {
        for (TcTvFromExcelDTO tcTOO : TcesDTO){
            List<Signal> types = this.convertToEntityAttribute(tcTOO.getType().replaceAll(" ", ""));
            List<WritableTc> tcesByLocOpT = repositoryWritableTcForImport.findByLocationIdAndOperatorIdAndTypeAndState(
                    repositoryLocation.findByFias(UUID.fromString(tcTOO.getFias())).getId(),
                    repositoryOperator.findByName(tcTOO.getOperator()).getId(),
                    TcTv.class.getAnnotation(DiscriminatorValue.class).value(),
                    TcState.ACTIVE
            );
            if (tcesByLocOpT.size() > 0) {
//                tcesByLocOpT.get(0).setTvOrRadioTypes(types);
                WritableTc clonedTc = tcesByLocOpT.get(0).cloneWithNullId();
                clonedTc.setTvOrRadioTypes(types);
                clonedTc = rWritableTc.save(clonedTc);
                FeatureEdit featureEdit = new FeatureEdit(tcesByLocOpT.get(0), clonedTc);
                featureEdit = repositoryFeatureEdits.save(featureEdit);
                LocationFeaturesEditingRequest importRequest = new LocationFeaturesEditingRequest(
                        tcesByLocOpT.get(0).getLocationId(),
                        "Импорт из файла",
                        user,
                        Collections.singleton(featureEdit)
                );
                importRequest.accept(serviceWritableTc);
                repositoryLocationFeaturesRequests.save(importRequest);
            } else {
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcTOO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcTOO.getOperator()).getId());
                tcByLocOpT.setTvOrRadioTypes(types);
                tcByLocOpT.setType(TcTv.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                // start journal
                rWritableTc.save(tcByLocOpT);
                FeatureEdit featureEdit = new FeatureEdit(tcByLocOpT, FeatureEditAction.CREATE);
                repositoryFeatureEdits.save(featureEdit);
                LocationFeaturesEditingRequest importRequest = new LocationFeaturesEditingRequest(
                        tcByLocOpT.getLocationId(),
                        "Импорт из файла",
                        user,
                        Collections.singleton(featureEdit)
                );
                repositoryLocationFeaturesRequests.save(importRequest);
                importRequest.accept(serviceWritableTc);
                // end journal
            }
            locationService.refreshCache();
        }
    }

    private List<Signal> convertToEntityAttribute(String s) {
        return Arrays
                .stream(s.split(","))
                .map(signalNameStr -> {
                    if (ATV.equals(signalNameStr.toUpperCase())) {
                        return Signal.ANLG;
                    } else if (CTV.equals(signalNameStr.toUpperCase())) {
                        return Signal.DIGT;
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
