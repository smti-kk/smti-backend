package ru.cifrak.telecomit.backend.api.service.imp.ap;

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

public class ApesFromExcelDTO implements ApesDTOFromExcel {

    private Workbook book;

    private final MultipartFile file;

    public ApesFromExcelDTO(MultipartFile file) {
        this.file = file;
    }

    @Override
    public MultipartFile getFile() {
        return file;
    }

    @Override
    public List<ApFromExcelDTO> getTcesDTO() {
        setBook(file);
        List<ApFromExcelDTO> tces = new ArrayList<>();
        Sheet sheet = this.book.getSheetAt(0);
        Row row;
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(15).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(17).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(19).setCellType(Cell.CELL_TYPE_STRING);
                if (this.notEmptyRow(row)) {
                    tces.add(new ApFromExcelDTO(row));
                }
            }
        }
        return tces;
    }

    private boolean notEmptyRow(Row row) {
        return !row.getCell(0).getStringCellValue().trim().isEmpty()
                || !row.getCell(5).getStringCellValue().trim().isEmpty()
                || !row.getCell(6).getStringCellValue().trim().isEmpty()
                || !row.getCell(7).getStringCellValue().trim().isEmpty()
                || !row.getCell(8).getStringCellValue().trim().isEmpty()
                || !row.getCell(9).getStringCellValue().trim().isEmpty()
                || !row.getCell(9).getStringCellValue().trim().isEmpty()
                || !row.getCell(10).getStringCellValue().trim().isEmpty()
                || !row.getCell(11).getStringCellValue().trim().isEmpty()
                || !row.getCell(12).getStringCellValue().trim().isEmpty()
                || !row.getCell(13).getStringCellValue().trim().isEmpty()
                || !row.getCell(14).getStringCellValue().trim().isEmpty()
                || !row.getCell(15).getStringCellValue().trim().isEmpty()
                || !row.getCell(19).getStringCellValue().trim().isEmpty();
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
