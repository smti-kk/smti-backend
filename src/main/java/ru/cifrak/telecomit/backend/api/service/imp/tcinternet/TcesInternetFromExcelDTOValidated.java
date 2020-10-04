package ru.cifrak.telecomit.backend.api.service.imp.tcinternet;

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
import ru.cifrak.telecomit.backend.repository.RepositoryTypeTruncChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TcesInternetFromExcelDTOValidated {

    private final RepositoryOperator repositoryOperator;

    private final RepositoryLocation repositoryLocation;

    private final RepositoryTypeTruncChannel repositoryTypeTruncChannel;

    private final TcesInternetDTOFromExcel origin;

    public TcesInternetFromExcelDTOValidated(
            RepositoryOperator repositoryOperator,
            RepositoryLocation repositoryLocation,
            RepositoryTypeTruncChannel repositoryTypeTruncChannel,
            TcesInternetDTOFromExcel origin) {
        this.repositoryOperator = repositoryOperator;
        this.repositoryLocation = repositoryLocation;
        this.repositoryTypeTruncChannel = repositoryTypeTruncChannel;
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

    public TcInternetImportResult getTcesDTO() throws FromExcelDTOFormatException,
            FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<TcInternetFromExcelDTO> tcesDTO = origin.getTcesInternetDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<TcInternetFromExcelDTO> toImport = new ArrayList<>();
        List<TcInternetFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (TcInternetFromExcelDTO tcDTO : origin.getTcesInternetDTO()) {
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
        return new TcInternetImportResult(
                importSuccess,
                importFailure,
                errorBookToByteStream(book),
                toImport
        );
    }

    private void checkTces(List<TcInternetFromExcelDTO> tcesInternetDTO)
            throws FromExcelDTOErrorException {
        String badTcInternetDTO;

        badTcInternetDTO = this.checkFullnessCells(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOErrorException("Не все ячейки заполнены.");
        }

        badTcInternetDTO = this.checkFiasesGUID(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС, должен быть в GUID формате.");
        }

        badTcInternetDTO = this.checkFiases(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС населённого пункта, не найден в БД.");
        }

        badTcInternetDTO = this.checkOperators(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, не найден в БД.");
        }

        badTcInternetDTO = this.checkOperatorsRights(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в операторе, данную Т/В могут предоставлять только {"
                    + repositoryOperator.internet()
                    .stream().map(Operator::getName).collect(Collectors.joining(", "))
                    + "}.");
        }

        badTcInternetDTO = this.checkChannel(tcesInternetDTO);
        if (badTcInternetDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе канала, не найден в БД.");
        }
    }

    private String checkChannel(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (repositoryTypeTruncChannel.findByName(TcInternetDTO.getChannel()) == null) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (repositoryLocation.findByFias(UUID.fromString(TcInternetDTO.getFias())) == null) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperators(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (repositoryOperator.findByName(TcInternetDTO.getOperator()) == null) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkOperatorsRights(List<TcInternetFromExcelDTO> tcesDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcDTO : tcesDTO) {
            if (!repositoryOperator.internet().contains(repositoryOperator.findByName(TcDTO.getOperator()))) {
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

    private boolean checkFullnessNpp(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        boolean result = true;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (TcInternetDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (TcInternetDTO.getFias().isEmpty()
                    || TcInternetDTO.getOperator().isEmpty()
                    || TcInternetDTO.getChannel().isEmpty()
            ) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiasesGUID(List<TcInternetFromExcelDTO> tcesInternetDTO) {
        String result = null;
        for (TcInternetFromExcelDTO TcInternetDTO : tcesInternetDTO) {
            if (!TcInternetDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = TcInternetDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
