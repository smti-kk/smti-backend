package ru.cifrak.telecomit.backend.api.service.imp.basestation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BaseStationsFromExcelDTOValidated  {

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

    public MultipartFile getFile() {
        return this.origin.getFile();
    }

    private Workbook createErrorBook() {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Ошибки");
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("Позиция");
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Описание");
        return book;
    }

    private void addError(Sheet sheet, int nRow, String npp, String error) {
        Row row = sheet.createRow(nRow);
        Cell cell = row.createCell(0);
        cell.setCellValue(npp);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        row.createCell(1).setCellValue(error);
    }

    private ByteArrayResource errorBookToByteStream (Workbook book) throws IOException {
        Sheet sheet = book.getSheetAt(0);
        sheet.autoSizeColumn(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        book.write(baos);
        book.close();
        return new ByteArrayResource(baos.toByteArray());
    }

    public BaseStationImportResult getTcesDTO() throws FromExcelDTOFormatException,
            FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<BaseStationFromExcelDTO> tcesDTO = origin.getTcesDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<BaseStationFromExcelDTO> toImport = new ArrayList<>();
        List<BaseStationFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (BaseStationFromExcelDTO tcDTO : origin.getTcesDTO()) {
            toCheck.clear();
            toCheck.add(tcDTO);
            try {
                checkTces(toCheck);
                toImport.add(tcDTO);
                importSuccess++;
            } catch (FromExcelDTOErrorException e) {
                importFailure++;
                addError(sheet, importFailure, tcDTO.getNpp(), e.getMessage());
            }
        }
        return new BaseStationImportResult(
                importSuccess,
                importFailure,
                errorBookToByteStream(book),
                toImport
        );
    }

    private void checkTces(List<BaseStationFromExcelDTO> tcesDTO)
            throws FromExcelDTOErrorException {
        String badDTO;

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Не все ячейки заполнены.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkType(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе мобильной связи, не найден в БД.");
        }

        badDTO = this.checkCommissioningFormat(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в дате ввода в эксплуатацию (" + badDTO + "), должна быть в формате ДД.ММ.ГГГГ.");
        }

        badDTO = this.checkCommissioningDate(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в дате ввода в эксплуатацию, она должна быть реальной.");
        }
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
