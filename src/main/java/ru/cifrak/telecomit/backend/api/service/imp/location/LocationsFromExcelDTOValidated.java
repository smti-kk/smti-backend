package ru.cifrak.telecomit.backend.api.service.imp.location;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOErrorException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTOFormatException;
import ru.cifrak.telecomit.backend.api.service.imp.FromExcelDTONppException;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationsFromExcelDTOValidated {

    private final RepositoryLocation repository;

    private final LocationsDTOFromExcel origin;

    public LocationsFromExcelDTOValidated(RepositoryLocation repository, LocationsDTOFromExcel origin) {
        this.repository = repository;
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

    public LocationImportResult getTcesDTO() throws FromExcelDTOFormatException,
            FromExcelDTONppException, IOException {
        this.checkFormatFile(this.getFile());
        List<LocationFromExcelDTO> tcesDTO = origin.getLocationsDTO();
        if (!this.checkFullnessNpp(tcesDTO)) {
            throw new FromExcelDTONppException("Не все \"№ п/п\" заполнены.");
        }
        int importFailure = 0;
        int importSuccess = 0;
        List<LocationFromExcelDTO> toImport = new ArrayList<>();
        List<LocationFromExcelDTO> toCheck = new ArrayList<>();
        Workbook book = createErrorBook();
        Sheet sheet = book.getSheetAt(0);
        for (LocationFromExcelDTO tcDTO : origin.getLocationsDTO()) {
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
        return new LocationImportResult(
                importSuccess,
                importFailure,
                errorBookToByteStream(book),
                toImport
        );
    }

    private void checkTces(List<LocationFromExcelDTO> locationsDTO)
            throws FromExcelDTOErrorException {
        String badLocationDTO;

        badLocationDTO = this.checkFullnessCells(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOErrorException("Не все ячейки заполнены.");
        }

        badLocationDTO = this.checkFiases(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в ФИАС, должен быть в GUID формате.");
        }

        badLocationDTO = this.checkTypes(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в типе, должен быть одним из {"
                    + String.join(", ", repository.findAllTypes()) + "}.");
        }

        badLocationDTO = this.checkPopulation(locationsDTO);
        if (badLocationDTO != null) {
            throw new FromExcelDTOErrorException("Ошибка в населении, должно быть в числовом формате.");
        }
    }

    private String checkPopulation(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!locationDTO.getPopulation().matches("[0-9]+")) {
                result = locationDTO.getNpp();
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

    private boolean checkFullnessNpp(List<LocationFromExcelDTO> locationsDTO) {
        boolean result = true;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (locationDTO.getNpp().isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private String checkFullnessCells(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (locationDTO.getTypeMO().isEmpty()
                    || locationDTO.getNameMO().isEmpty()
                    || locationDTO.getType().isEmpty()
                    || locationDTO.getName().isEmpty()
                    || locationDTO.getFias().isEmpty()
                    || locationDTO.getPopulation().isEmpty()
            ) {
                result = locationDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkFiases(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!locationDTO.getFias()
                    .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                result = locationDTO.getNpp();
                break;
            }
        }
        return result;
    }

    private String checkTypes(List<LocationFromExcelDTO> locationsDTO) {
        String result = null;
        // TODO: List<String> -> List<Locations>
        List<String> typesOfLocationsDTO = repository.findAllTypes();
        for (LocationFromExcelDTO locationDTO : locationsDTO) {
            if (!typesOfLocationsDTO.contains(locationDTO.getType())
                    || !typesOfLocationsDTO.contains(locationDTO.getTypeMO())) {
                result = locationDTO.getNpp();
                break;
            }
        }
        return result;
    }
}
