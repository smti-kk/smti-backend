package ru.cifrak.telecomit.backend.api.service.imp.tctv;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.entities.Signal;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TcesTvFromExcelDTOValidated implements TcesTvDTOFromExcel {

    private final static String ATV = "АТВ";

    private final static String CTV = "ЦТВ";

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final TcesTvDTOFromExcel origin;

    public TcesTvFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            TcesTvDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<TcTvFromExcelDTO> getTcesTvDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTcesTv(origin.getTcesTvDTO());
    }

    private List<TcTvFromExcelDTO> checkTcesTv(List<TcTvFromExcelDTO> tcesTvDTO)
            throws FromExcelDTOFormatException {
        String badDTO;

        if (!this.checkFullnessNpp(tcesTvDTO)) {
            throw new FromExcelDTOFormatException("Not all npp are filled.");
        }

        badDTO = this.checkFullnessCells(tcesTvDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO + " position not all cells are filled.");
        }

        badDTO = this.checkFiasesGUID(tcesTvDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO
                    + " position FIAS error, must be in GUID-format.");
        }

        badDTO = this.checkFiases(tcesTvDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO
                    + " position location FIAS error, not found in BD.");
        }

        badDTO = this.checkOperators(tcesTvDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO
                    + " position operator error, not found in BD.");
        }

        badDTO = this.checkType(tcesTvDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("In " + badDTO
                    + " position type error, each element must be in {" + ATV + ", " + CTV +"}.");
        }

        return tcesTvDTO;
    }

    private String checkType(List<TcTvFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcTvFromExcelDTO TcDTO : tcesDTO) {
            if (this.convertToEntityAttribute(TcDTO.getType().replaceAll(" ", "")).contains(null)) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private List<Signal> convertToEntityAttribute(String s) {
        return Arrays
                .stream(s.split(","))
                .map(signalNameStr -> {
                    if (ATV.equals(signalNameStr.toUpperCase())) {
                        return Signal.ANLG;
                    } else if (CTV.equals(signalNameStr.toUpperCase())) {
                        return Signal.DIGT;
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    private String checkFiases(List<TcTvFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcTvFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcDTO.getFias())) == null) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcTvFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcTvFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryOperator.findByName(TcDTO.getOperator()) == null) {
                result = TcDTO.getNpp();
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

    private boolean checkFullnessNpp(List<TcTvFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (TcTvFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcTvFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcTvFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getFias().isEmpty()
                    || TcDTO.getOperator().isEmpty()
                    || TcDTO.getType().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcTvFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcTvFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
