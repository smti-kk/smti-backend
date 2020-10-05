package ru.cifrak.telecomit.backend.api.service.imp.tcats;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryOperator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TcesAtsFromExcelDTOValidated {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final TcesAtsDTOFromExcel origin;

    public TcesAtsFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            TcesAtsDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
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

    public TcAtsImportResult getTcesDTO() throws FromExcelDTOFormatException,
            FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<TcAtsFromExcelDTO> tcesDTO = origin.getTcesDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<TcAtsFromExcelDTO> toImport = new ArrayList<>();
        List<TcAtsFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (TcAtsFromExcelDTO tcDTO : origin.getTcesDTO()) {
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
        return new TcAtsImportResult(
                importSuccess,
                importFailure,
                errorBookToByteStream(book),
                toImport
        );
    }

    private void checkTces(List<TcAtsFromExcelDTO> tcesDTO)
            throws FromExcelDTOErrorException {
        String badDTO;

        badDTO = this.checkFullnessCells(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Не все ячейки заполнены.");
        }

        badDTO = this.checkFiasesGUID(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС, должен быть в GUID формате.");
        }

        badDTO = this.checkFiases(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badDTO = this.checkOperators(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, не найден в БД.");
        }

        badDTO = this.checkOperatorsRights(tcesDTO);
        if (badDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, данную Т/В могут предоставлять только {"
                    + repositoryOperator.ats()
                    .stream().map(Operator::getName).collect(Collectors.joining(", "))
                    + "}.");
        }
    }

    private String checkFiases(List<TcAtsFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcAtsFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcDTO.getFias())) == null) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcAtsFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcAtsFromExcelDTO TcDTO : tcesDTO) {
            if (repositoryOperator.findByName(TcDTO.getOperator()) == null) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperatorsRights(List<TcAtsFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcAtsFromExcelDTO TcDTO : tcesDTO) {
            if (!repositoryOperator.ats().contains(repositoryOperator.findByName(TcDTO.getOperator()))) {
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

    private boolean checkFullnessNpp(List<TcAtsFromExcelDTO> tcesDTO) {
        boolean result = true;
        for (TcAtsFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcAtsFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcAtsFromExcelDTO TcDTO : tcesDTO) {
            if (TcDTO.getFias().isEmpty()
                    || TcDTO.getOperator().isEmpty()
            ) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcAtsFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcAtsFromExcelDTO TcDTO : tcesDTO) {
            if (!TcDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
