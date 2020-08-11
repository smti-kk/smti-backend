package ru.cifrak.telecomit.backend.api.service.imp.tcmobile;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class TcesMobileFromExcelDTOValidated implements TcesMobileDTOFromExcel {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryMobileType repositoryMobileType;

    private final TcesMobileDTOFromExcel origin;

    public TcesMobileFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            RepositoryMobileType repositoryMobileType,
            TcesMobileDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
        this.repositoryMobileType = repositoryMobileType;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<TcMobileFromExcelDTO> getTcesMobileDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTcesMobile(origin.getTcesMobileDTO());
    }

    private List<TcMobileFromExcelDTO> checkTcesMobile(List<TcMobileFromExcelDTO> tcesMobileDTO)
            throws FromExcelDTOFormatException {
        String badTcMobileDTO;

        if (!this.checkFullnessNpp(tcesMobileDTO)) {
            throw new FromExcelDTOFormatException("Not all npp are filled.");
        }

        badTcMobileDTO = this.checkFullnessCells(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOFormatException("In " + badTcMobileDTO + " position not all cells are filled.");
        }

        badTcMobileDTO = this.checkFiasesGUID(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOFormatException("In " + badTcMobileDTO
                    + " position FIAS error, must be in GUID-format.");
        }

        badTcMobileDTO = this.checkFiases(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOFormatException("In " + badTcMobileDTO
                    + " position location FIAS error, not found in BD.");
        }

        badTcMobileDTO = this.checkOperators(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOFormatException("In " + badTcMobileDTO
                    + " position operator error, not found in BD.");
        }

        badTcMobileDTO = this.checkType(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOFormatException("In " + badTcMobileDTO
                    + " position type error, not found in BD.");
        }

        return tcesMobileDTO;
    }

    private String checkType(List<TcMobileFromExcelDTO> tcesMobileDTO) {
        String result = null;
        for (TcMobileFromExcelDTO TcMobileDTO : tcesMobileDTO) {
            if (repositoryMobileType.findByName(TcMobileDTO.getType()) == null) {
                result = TcMobileDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<TcMobileFromExcelDTO> tcesMobileDTO) {
        String result = null;
        for (TcMobileFromExcelDTO TcMobileDTO : tcesMobileDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcMobileDTO.getFias())) == null) {
                result = TcMobileDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcMobileFromExcelDTO> tcesMobileDTO) {
        String result = null;
        for (TcMobileFromExcelDTO TcMobileDTO : tcesMobileDTO) {
            if (repositoryOperator.findByName(TcMobileDTO.getOperator()) == null) {
                result = TcMobileDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private void checkFormatFile(MultipartFile file) throws FromExcelDTOFormatException {
        boolean result;
        InputStream is = this.checkIOFile(file);
        if (is == null) {
            result = false;
        } else {
            result = this.checkExcelFormat(is);
        }
        if (!result) {
            throw new FromExcelDTOFormatException("Wrong file type.");
        }
    }

    private InputStream checkIOFile(MultipartFile file) {
        InputStream result;
        try {
            result = file.getInputStream();
        } catch (IOException e) {
            result = null;
        }
        return result;
    }

    private boolean checkExcelFormat(InputStream is) {
        boolean result = true;
        try {
            new XSSFWorkbook(is);
        } catch (Exception eXSSF) {
//            try {
//                new HSSFWorkbook(is);
//            } catch (Exception eHSSF) {
                result = false;
//            }
        }
        return result;
    }

    private boolean checkFullnessNpp(List<TcMobileFromExcelDTO> tcesMobileDTO) {
        boolean result = true;
        for (TcMobileFromExcelDTO TcMobileDTO : tcesMobileDTO) {
            if (TcMobileDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcMobileFromExcelDTO> tcesMobileDTO) {
        String result = null;
        for (TcMobileFromExcelDTO TcMobileDTO : tcesMobileDTO) {
            if (TcMobileDTO.getFias().isEmpty()
                    || TcMobileDTO.getOperator().isEmpty()
                    || TcMobileDTO.getType().isEmpty()
            ) {
                result = TcMobileDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcMobileFromExcelDTO> tcesMobileDTO) {
        String result = null;
        for (TcMobileFromExcelDTO TcMobileDTO : tcesMobileDTO) {
            if (!TcMobileDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcMobileDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
