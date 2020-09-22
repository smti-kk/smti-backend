package ru.cifrak.telecomit.backend.api.service.imp.tctv;

import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;

import java.util.List;

public interface TcesTvDTOFromExcel {

    List<TcTvFromExcelDTO> getTcesTvDTO() throws FromExcelDTOFormatException;

    MultipartFile getFile();
}
