package ru.cifrak.telecomit.backend.api.service.imp.tcmobile;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface TcesMobileDTOFromExcel {

    List<TcMobileFromExcelDTO> getTcesMobileDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
