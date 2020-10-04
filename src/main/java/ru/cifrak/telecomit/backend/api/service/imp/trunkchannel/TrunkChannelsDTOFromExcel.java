package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface TrunkChannelsDTOFromExcel {

    List<TrunkChannelFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
