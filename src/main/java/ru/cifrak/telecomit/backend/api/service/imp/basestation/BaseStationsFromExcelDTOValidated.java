package ru.cifrak.telecomit.backend.api.service.imp.basestation;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BaseStationsFromExcelDTOValidated implements BaseStationsDTOFromExcel {

    private final BaseStationsDTOFromExcel origin;

    private final RepositoryOperator repositoryOperator;

    private final RepositoryMobileType repositoryMobileType;

    public BaseStationsFromExcelDTOValidated(
            BaseStationsDTOFromExcel origin,
            RepositoryOperator repositoryOperator,
            RepositoryMobileType repositoryMobileType) {
        this.origin = origin;
        this.repositoryOperator = repositoryOperator;
        this.repositoryMobileType = repositoryMobileType;
    }

    @Override
    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    @Override
    public List<BaseStationFromExcelDTO> getTcesDTO() throws FromExcelDTOFormatException {
        this.checkFormatFile(this.getFile());

        return this.checkTces(origin.getTcesDTO());
    }

    private List<BaseStationFromExcelDTO> checkTces(List<BaseStationFromExcelDTO> tcesDTO)
            throws FromExcelDTOFormatException {
        String badDTO;

        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTOFormatException("Не все \"№ п/п\" заполнены.");
        }

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO + " позиции не все ячейки заполнены.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkType(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в типе мобильной связи, не найден в БД.");
        }

        badDTO = this.checkCommissioningFormat(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в дате ввода в эксплуатацию (" + badDTO + "), должна быть в формате ДД.ММ.ГГГГ.");
        }

        badDTO = this.checkCommissioningDate(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOFormatException("В " + badDTO
                    + " позиции ошибка в дате ввода в эксплуатацию, она должна быть реальной.");
        }

        return tcesDTO;
    }

    private String checkType(List<BaseStationFromExcelDTO> tcesDTO) {
        String result = null;
        for (BaseStationFromExcelDTO tcDTO : tcesDTO) {
            if (repositoryMobileType.findByName(tcDTO.getMobileType()) == null) {
                result = tcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<BaseStationFromExcelDTO> tcesDTO) {
        String result = null;
        for (BaseStationFromExcelDTO tcDTO : tcesDTO) {
            if (repositoryOperator.findByName(tcDTO.getOperator()) == null) {
                result = tcDTO.getNpp();
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

    private boolean checkFullnessNpp(List<BaseStationFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (BaseStationFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<BaseStationFromExcelDTO> tcesDTO) {
        String result = null;
        for (BaseStationFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getAddress().isEmpty()
                    || TcDTO.getLatitude().isEmpty()
                    || TcDTO.getLongitude().isEmpty()
                    || TcDTO.getOperator().isEmpty()
                    || TcDTO.getMobileType().isEmpty()
                    || TcDTO.getCoverageRadius().isEmpty()
                    || TcDTO.getPropHeight().isEmpty()
                    || TcDTO.getActionDate().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkCommissioningFormat(List<BaseStationFromExcelDTO> tcesDTO) {
        String result = null;
        for (BaseStationFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getActionDate().isEmpty()
                    && !TcDTO.getActionDate().matches("[0-9]{2}.[0-9]{2}.[0-9]{4}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkCommissioningDate(List<BaseStationFromExcelDTO> tcesDTO) {
        String result = null;
        for (BaseStationFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getActionDate().isEmpty()) {
                continue;
            }
            int yyyy = Integer.parseInt(TcDTO.getActionDate().substring(6, 10));
            int mm = Integer.parseInt(TcDTO.getActionDate().substring(3, 5)) - 1;
            int dd = Integer.parseInt(TcDTO.getActionDate().substring(0, 2));
            GregorianCalendar dateBefore50Years = new GregorianCalendar();
            dateBefore50Years.add(Calendar.YEAR, -50);
            GregorianCalendar dateAfter50Years = new GregorianCalendar();
            dateAfter50Years.add(Calendar.YEAR, +50);
            GregorianCalendar checkedDate = new GregorianCalendar(yyyy, mm, dd);
            if (yyyy != checkedDate.get(Calendar.YEAR)
                    || mm != checkedDate.get(Calendar.MONTH)
                    || dd != checkedDate.get(Calendar.DAY_OF_MONTH)
                    || checkedDate.before(dateBefore50Years)
                    || checkedDate.after(dateAfter50Years)) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
