package ru.cifrak.telecomit.backend.api.service.imp.tcinfomat;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface TcesInfomatDTOFromExcel {

    List<TcInfomatFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
