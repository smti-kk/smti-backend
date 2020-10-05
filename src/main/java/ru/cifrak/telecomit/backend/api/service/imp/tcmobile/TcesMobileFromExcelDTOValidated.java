package ru.cifrak.telecomit.backend.api.service.imp.tcmobile;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TcesMobileFromExcelDTOValidated {

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
        cell.setCellValue(Integer.parseInt(npp));
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

    public TcMobileImportResult getTcesDTO() throws FromExcelDTOFormatException,
            FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<TcMobileFromExcelDTO> tcesDTO = origin.getTcesMobileDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<TcMobileFromExcelDTO> toImport = new ArrayList<>();
        List<TcMobileFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (TcMobileFromExcelDTO tcDTO : origin.getTcesMobileDTO()) {
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
        return new TcMobileImportResult(
                importSuccess,
                importFailure,
                errorBookToByteStream(book),
                toImport
        );
    }

    private void checkTces(List<TcMobileFromExcelDTO> tcesMobileDTO)
            throws FromExcelDTOErrorException {
        String badTcMobileDTO;

        badTcMobileDTO = this.checkFullnessCells(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOErrorException("Не все ячейки заполнены.");
        }

        badTcMobileDTO = this.checkFiasesGUID(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС, должен быть в GUID формате.");
        }

        badTcMobileDTO = this.checkFiases(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badTcMobileDTO = this.checkOperators(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, не найден в БД.");
        }

        badTcMobileDTO = this.checkOperatorsRights(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, данную Т/В могут предоставлять только {"
                    + repositoryOperator.mobile()
                    .stream().map(Operator::getName).collect(Collectors.joining(", "))
                    + "}.");
        }

        badTcMobileDTO = this.checkType(tcesMobileDTO);
        if (badTcMobileDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе, не найден в БД.");
        }
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

    private String checkOperatorsRights(List<TcMobileFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcMobileFromExcelDTO TcDTO : tcesDTO) {
            if (!repositoryOperator.mobile().contains(repositoryOperator.findByName(TcDTO.getOperator()))) {
                result = TcDTO.getNpp();
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
