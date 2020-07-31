package ru.cifrak.telecomit.backend.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LocationsDTOFromExcel {

    List<LocationFromExcelDTO> getLocationsDTO() throws LocationDTOFormatException;

    MultipartFile getFile();
}
