package ru.cifrak.telecomit.backend.api.service.imp.basestation;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface BaseStationsDTOFromExcel {

    List<BaseStationFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
