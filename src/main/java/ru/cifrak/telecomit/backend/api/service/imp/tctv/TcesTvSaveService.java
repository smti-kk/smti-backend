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
        for (TcTvFromExcelDTO tcDTO : TcesDTO){
            List<Signal> types = this.convertToEntityAttribute(tcDTO.getType().replaceAll(" ", ""));
            List<WritableTc> tcesByLocOpT = repositoryWritableTcForImport.findByLocationIdAndOperatorIdAndTypeAndState(
                    repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId(),
                    repositoryOperator.findByName(tcDTO.getOperator()).getId(),
                    TcTv.class.getAnnotation(DiscriminatorValue.class).value(),
                    TcState.ACTIVE
            );
            if (tcesByLocOpT.size() > 0) {
//                tcesByLocOpT.get(0).setTvOrRadioTypes(types);
                WritableTc clonedTc = tcesByLocOpT.get(0).cloneWithNullId();
                clonedTc.setTvOrRadioTypes(types);
                clonedTc = rWritableTc.save(clonedTc);
                FeatureEdit featureEdit = new FeatureEdit(tcesByLocOpT.get(0), clonedTc);
                featureEdit.setAction(tcDTO.getActivity().equalsIgnoreCase("нет") ? FeatureEditAction.DELETE
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
                if (tcDTO.getActivity().equalsIgnoreCase("нет")) {
                    continue;
                }
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcDTO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcDTO.getOperator()).getId());
                tcByLocOpT.setTvOrRadioTypes(types);
                tcByLocOpT.setType(TcTv.class.getAnnotation(DiscriminatorValue.class).value());
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
