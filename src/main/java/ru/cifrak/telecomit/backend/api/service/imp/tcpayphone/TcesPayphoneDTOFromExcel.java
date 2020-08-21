package ru.cifrak.telecomit.backend.api.service.imp.tcpayphone;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface TcesPayphoneDTOFromExcel {

    List<TcPayphoneFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
