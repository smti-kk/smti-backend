package ru.cifrak.telecomit.backend.api.service.imp.ap;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface ApesDTOFromExcel {

    List<? extends ApFromExcelDTO> getTcesDTO(String apType) throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
