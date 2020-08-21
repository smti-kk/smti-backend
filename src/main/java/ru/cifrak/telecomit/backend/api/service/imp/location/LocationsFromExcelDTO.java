package ru.cifrak.telecomit.backend.api.service.imp.location;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationsFromExcelDTO implements LocationsDTOFromExcel {

    private Workbook book;

    private final MultipartFile file;

//    private enum ExcelFormat {XLS, XLSX};

    public LocationsFromExcelDTO(MultipartFile file) {
        this.file = file;
    }

    @Override
    public MultipartFile getFile() {
        return file;
    }

    @Override
    public List<LocationFromExcelDTO> getLocationsDTO() {
        setBook(file);
        List<LocationFromExcelDTO> locations = new ArrayList<>();
        Sheet sheet = this.book.getSheetAt(0);
        Row row;
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                if (this.notEmptyRow(row)) {
                    locations.add(new LocationFromExcelDTO(row));
                }
            }
        }
        return locations;
    }

    private boolean notEmptyRow(Row row) {
        return !row.getCell(0).getStringCellValue().trim().isEmpty()
                || !row.getCell(1).getStringCellValue().trim().isEmpty()
                || !row.getCell(2).getStringCellValue().trim().isEmpty()
                || !row.getCell(5).getStringCellValue().trim().isEmpty()
                || !row.getCell(4).getStringCellValue().trim().isEmpty()
                || !row.getCell(6).getStringCellValue().trim().isEmpty()
                || !row.getCell(3).getStringCellValue().trim().isEmpty();
    }

//    private ExcelFormat getFormat(String file) {
//        ExcelFormat format = ExcelFormat.XLSX;
//        if (file.toLowerCase().endsWith(".xls")) {
//            format = ExcelFormat.XLS;
//        }
//        return format;
//    }

    @SneakyThrows
    public void setBook(MultipartFile file) {
//        ExcelFormat format = getFormat(file.getOriginalFilename());
        InputStream is = file.getInputStream();
        try {
            this.book = new XSSFWorkbook(is);
        } catch (Exception eXSSF) {
            this.book = new HSSFWorkbook(is);
        }
        this.book.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

//    private void getExcelFormatFile(MultipartFile file) throws LocationDTOFormatException {
//        boolean result;
//        InputStream is = this.checkIOFile(file);
//        if (is == null) {
//            result = false;
//        } else {
//            result = this.checkExcelFormat(is);
//        }
//        if (!result) {
//            throw new LocationDTOFormatException("Неправильный тип файла.");
//        }
//    }
//
//    private InputStream checkIOFile(MultipartFile file) {
//        InputStream result;
//        try {
//            result = file.getInputStream();
//        } catch (IOException e) {
//            result = null;
//        }
//        return result;
//    }
//
//    private boolean checkExcelFormat(InputStream is) {
//        boolean result = true;
//        try {
//            new XSSFWorkbook(is);
//        } catch (Exception eXSSF) {
//            try {
//                new HSSFWorkbook(is);
//            } catch (Exception eHSSF) {
//                result = false;
//            }
//        }
//        return result;
//    }
}
