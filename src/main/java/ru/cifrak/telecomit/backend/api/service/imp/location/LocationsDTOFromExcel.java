package ru.cifrak.telecomit.backend.api.service.imp.location;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface LocationsDTOFromExcel {

    List<LocationFromExcelDTO> getLocationsDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
