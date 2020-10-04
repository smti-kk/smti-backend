package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ImportResultTrunkChannel {

    private int importSuccess;

    private int importFailure;

    private ByteArrayResource fileWithError;

    private List<TrunkChannelFromExcelDTO>  listToImport;
}
