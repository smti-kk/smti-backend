package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;

import java.util.List;

public interface TrunkChannelsDTOFromExcel {

    List<TrunkChannelFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
