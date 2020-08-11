package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface TcesInternetDTOFromExcel {

    List<TcInternetFromExcelDTO> getTcesInternetDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
