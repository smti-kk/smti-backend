package ru.cifrak.telecomit.backend.api.service.imp.tcpayphone;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class TcesPayphoneFromExcelDTOValidated implements TcesPayphoneDTOFromExcel {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final TcesPayphoneDTOFromExcel origin;

    public TcesPayphoneFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            TcesPayphoneDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
        this.origin = origin;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<TcPayphoneFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTces(origin.getTcesDTO());
    }

    private List<TcPayphoneFromExcelDTO> checkTces(List<TcPayphoneFromExcelDTO> tcesDTO)
            throws FromExcelDTOFormatException {
        String badDTO;

        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTOFormatException("Не все \"№ п/п\" заполнены.");
        }

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO + " позиции не все ячейки заполнены.");
        }

        badDTO = this.checkFiasesGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС, должен быть в GUID формате.");
        }

        badDTO = this.checkFiases(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkQuantity(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в количестве, должно быть в числовом формате.");
        }

        return tcesDTO;
    }

    private String checkQuantity(List<TcPayphoneFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPayphoneFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getQuantity().matches("[0-9]+")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<TcPayphoneFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPayphoneFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcDTO.getFias())) == null) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcPayphoneFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPayphoneFromExcelDTO TcDTO : tcesDTO) {
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
            throw new FromExcelDTOFormatException("Неправильный тип файла.");
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

    private boolean checkFullnessNpp(List<TcPayphoneFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (TcPayphoneFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcPayphoneFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPayphoneFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getFias().isEmpty()
                    || TcDTO.getOperator().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcPayphoneFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcPayphoneFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
