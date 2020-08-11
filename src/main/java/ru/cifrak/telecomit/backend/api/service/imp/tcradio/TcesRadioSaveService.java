package ru.cifrak.telecomit.backend.api.service.imp.tcradio;

import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.Signal;
import ru.cifrak.telecomit.backend.entities.TcRadio;
import ru.cifrak.telecomit.backend.entities.TcTv;
import ru.cifrak.telecomit.backend.entities.locationsummary.WritableTc;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;
import ru.cifrak.telecomit.backend.repository.RepositoryWritableTc;

import javax.persistence.DiscriminatorValue;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TcesRadioSaveService {

    private final static String ATV = "АТВ";

    private final static String CTV = "ЦТВ";

    private final RepositoryWritableTc repositoryWritableTc;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryOperator repositoryOperator;

    public TcesRadioSaveService(
            RepositoryWritableTc repositoryWritableTc,
            RepositoryLocation repositoryLocation,
            RepositoryOperator repositoryOperator) {
        this.repositoryWritableTc = repositoryWritableTc;
        this.repositoryLocation = repositoryLocation;
        this.repositoryOperator = repositoryOperator;
    }

    public void saveTces(List<TcRadioFromExcelDTO> TcesDTO) {
        for (TcRadioFromExcelDTO tcTOO : TcesDTO){
            List<Signal> types = this.convertToEntityAttribute(tcTOO.getType().replaceAll(" ", ""));
            List<WritableTc> tcesByLocOpT = repositoryWritableTc.findByLocationIdAndOperatorIdAndType(
                    repositoryLocation.findByFias(UUID.fromString(tcTOO.getFias())).getId(),
                    repositoryOperator.findByName(tcTOO.getOperator()).getId(),
                    TcRadio.class.getAnnotation(DiscriminatorValue.class).value()
            );
            if (tcesByLocOpT.size() > 0) {
                tcesByLocOpT.get(0).setTvOrRadioTypes(types);
                // TODO: Transaction.
                repositoryWritableTc.save(tcesByLocOpT.get(0));
            } else {
                WritableTc tcByLocOpT = new WritableTc();
                tcByLocOpT.setLocationId(repositoryLocation.findByFias(UUID.fromString(tcTOO.getFias())).getId());
                tcByLocOpT.setOperatorId(repositoryOperator.findByName(tcTOO.getOperator()).getId());
                tcByLocOpT.setTvOrRadioTypes(types);
                tcByLocOpT.setType(TcRadio.class.getAnnotation(DiscriminatorValue.class).value());
                tcByLocOpT.setQuality(ServiceQuality.NORMAL);
                // TODO: Transaction.
                repositoryWritableTc.save(tcByLocOpT);
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
