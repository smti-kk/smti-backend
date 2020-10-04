package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import ru.cifrak.telecomit.backend.api.service.imp.trunkchannel.TrunkChannelFromExcelDTO;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class TcInternetImportResult {

    private int importSuccess;

    private int importFailure;

    private ByteArrayResource fileWithError;

    private List<TcInternetFromExcelDTO> listToImport;
}
