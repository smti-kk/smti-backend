package ru.cifrak.telecomit.backend.api.service.imp.tcpost;

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

public class TcesPostFromExcelDTO implements TcesPostDTOFromExcel {

    private Workbook book;

    private final MultipartFile file;

    public TcesPostFromExcelDTO(MultipartFile file) {
        this.file = file;
    }

    @Override
    public MultipartFile getFile() {
        return file;
    }

    @Override
    public List<TcPostFromExcelDTO> getTcesDTO() {
        setBook(file);
        List<TcPostFromExcelDTO> tces = new ArrayList<>();
        Sheet sheet = this.book.getSheetAt(0);
        Row row;
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                if (this.notEmptyRow(row)) {
                    tces.add(new TcPostFromExcelDTO(row));
                }
            }
        }
        return tces;
    }

    private boolean notEmptyRow(Row row) {
        return !row.getCell(0).getStringCellValue().trim().isEmpty()
                || !row.getCell(5).getStringCellValue().trim().isEmpty()
                || !row.getCell(6).getStringCellValue().trim().isEmpty()
                || !row.getCell(7).getStringCellValue().trim().isEmpty();
    }

    @SneakyThrows
    public void setBook(MultipartFile file) {
        InputStream is = file.getInputStream();
        try {
            this.book = new XSSFWorkbook(is);
        } catch (Exception eXSSF) {
            this.book = new HSSFWorkbook(is);
        }
        this.book.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
    }
}
