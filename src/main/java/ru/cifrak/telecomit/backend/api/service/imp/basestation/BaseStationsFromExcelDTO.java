package ru.cifrak.telecomit.backend.api.service.imp.basestation;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

public class BaseStationsFromExcelDTO implements BaseStationsDTOFromExcel {

    private Workbook book;

    private final MultipartFile file;

    public BaseStationsFromExcelDTO(MultipartFile file) {
        this.file = file;
    }

    @Override
    public MultipartFile getFile() {
        return file;
    }

    @Override
    public List<BaseStationFromExcelDTO> getTcesDTO() {
        setBook(file);
        List<BaseStationFromExcelDTO> tces = new ArrayList<>();
        Sheet sheet = this.book.getSheetAt(0);
        Row row;
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
                if (this.notEmptyRow(row)) {
                    tces.add(new BaseStationFromExcelDTO(row, formatCommissioning(row.getCell(8))));
                }
            }
        }
        return tces;
    }

    private String formatCommissioning(Cell cell) {
        String result = null;
        Date date = null;
        try {
            date = cell.getDateCellValue();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int yyyy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH) + 1;
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            result = String.format("%02d", dd)
                    + "." + String.format("%02d", mm)
                    + "." + yyyy;
        } catch (Exception e) {
            if (date != null) {
                result = cell.getStringCellValue();
            } else {
                if (((XSSFCell) cell).getRawValue() == null) {
                    result = "";
                } else {
                    result = "00.00.0000";
                }
            }
        }
        return result;
    }

    private boolean notEmptyRow(Row row) {
        return !row.getCell(0).getStringCellValue().trim().isEmpty()
                || !row.getCell(1).getStringCellValue().trim().isEmpty()
                || !row.getCell(2).getStringCellValue().trim().isEmpty()
                || !row.getCell(3).getStringCellValue().trim().isEmpty()
                || !row.getCell(4).getStringCellValue().trim().isEmpty()
                || !row.getCell(5).getStringCellValue().trim().isEmpty()
                || !row.getCell(6).getStringCellValue().trim().isEmpty()
                || !row.getCell(7).getStringCellValue().trim().isEmpty()
                || !row.getCell(8).getStringCellValue().trim().isEmpty();
    }

    @SneakyThrows
    public void setBook(MultipartFile file) {
        InputStream is = file.getInputStream();
        try {
            this.book = new XSSFWorkbook(is);
        } catch (Exception eXSSF) {
            this.book = new HSSFWorkbook(is);
        }
        this.book.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }
}
