package ru.cifrak.telecomit.backend.api.service.imp.tctv;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.Signal;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.TcTv;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTcForImport;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTcForImport;

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

    public TcesTvSaveService(
            RepositoryWritableTcForImport repositoryWritableTcForImport,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator) {
        this.repositoryWritableTcForImport = repositoryWritableTcForImport;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
    }

    public void saveTces(List<TcTvFromExcelDTO> TcesDTO) {
        for (TcTvFromExcelDTO tcTOO : TcesDTO){
            List<Signal> types = this.convertToEntityAttribute(tcTOO.getType().replaceAll(" ", ""));
            List<WritableTcForImport> tcesByLocOpT = repositoryWritableTcForImport.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcTOO.getFias())).getId(),
                    repositoryOperator.findByName(tcTOO.getOperator()).getId(),
                    TcTv.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setTvOrRadioTypes(types);
                tcesByLocOpT.get(0).setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTcForImport.save(tcesByLocOpT.get(0));
            } else {
                WritableTcForImport tcByLocOpT = new WritableTcForImport();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcTOO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcTOO.getOperator()).getId());
                tcByLocOpT.setTvOrRadioTypes(types);
                tcByLocOpT.setType(TcTv.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                tcByLocOpT.setState(TcState.ACTIVE);
                // TODO: Transaction.
                repositoryWritableTcForImport.save(tcByLocOpT);
            }
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
