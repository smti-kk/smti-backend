package ru.cifrak.telecomit.backend.api.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.Location;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ParsingExcelLocations {

    private Workbook book;

    private enum ExcelFormat {XLS, XLSX};

    public ParsingExcelLocations(MultipartFile file) throws IOException {
        this.setBook(file);
    }

    private ExcelFormat getFormat(String file) {
        ExcelFormat format = ExcelFormat.XLSX;
        if (file.toLowerCase().endsWith(".xls")) {
            format = ExcelFormat.XLS;
        }
        return format;
    }

    public void setBook(MultipartFile file) throws IOException {
        ExcelFormat format = getFormat(file.getOriginalFilename());
        InputStream is = file.getInputStream();
        if (format == ExcelFormat.XLSX) {
            this.book = new XSSFWorkbook(is);
        } else {
            this.book = new HSSFWorkbook(is);
        }
        this.book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
    }

    private String printCell(Cell cell) {
        StringBuilder result = new StringBuilder();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                result.append(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                result.append(cell.getNumericCellValue());
                break;
        }
        result.append(';');
        return result.toString();
    }

    public List<LocationFromExcelDTO> getLocationsDTO() {
        List<LocationFromExcelDTO> locations = new ArrayList<>();
        Sheet sheet = this.book.getSheetAt(0);
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            locations.add(getDataFromRow(sheet.getRow(i)));
        }
        return locations;
    }

    private LocationFromExcelDTO getDataFromRow(Row row) {
        return new LocationFromExcelDTO(
                UUID.fromString(row.getCell(5).getStringCellValue().trim()),
                row.getCell(4).getStringCellValue().trim(),
                (int) row.getCell(6).getNumericCellValue(),
                row.getCell(4).getStringCellValue().trim()
        );
    }

    public String readFile() {
        StringBuilder result = new StringBuilder();
        Sheet sheet;
        for (int i = 0; i < this.book.getNumberOfSheets(); i++) {
            sheet = this.book.getSheetAt(i);
            result.append("\nSheet ")
                    .append(sheet.getSheetName())
                    .append('\n');
            for (Row row : sheet) {
                for (Cell cell : row) {
                    result.append(printCell(cell));
                }
                result.append('\n');
            }
        }
        return result.toString();
    }
}
