package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;

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
    public List<? extends ApFromExcelDTO> getTcesDTO(String apType) {
        List tces;
        int rowCount = 0;
        switch (TypeAccessPoint.valueOf(apType)) {
            case ESPD:
                tces = new ArrayList<ApESPDFromExcelDTO>();
                rowCount = 28;
                break;
            case SMO:
                tces = new ArrayList<ApSMOFromExcelDTO>();
                rowCount = 23;
                break;
            default:
                return null;
        }

        setBook(file);
        Sheet sheet = this.book.getSheetAt(0);
        Row row;

        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                // Все значимые ячейки таблицы приводятся к строковому типу
                for (int j = 0; j < rowCount; j++) {
                    row.getCell(j).setCellType(CellType.STRING);
                }

                if (this.notEmptyRow(row, apType)) {
                    switch (TypeAccessPoint.valueOf(apType)) {
                        case ESPD:
                            tces.add(new ApESPDFromExcelDTO(row));
                            break;
                        case SMO:
                            tces.add(new ApSMOFromExcelDTO(row));
                            break;
                    }
                }
            }
        }
        return tces;
    }

    private boolean notEmptyRow(Row row, String apType) {
        switch (TypeAccessPoint.valueOf(apType)) {
            case ESPD:
                return     !row.getCell(0).getStringCellValue().trim().isEmpty()
                        || !row.getCell(5).getStringCellValue().trim().isEmpty()
                        || !row.getCell(6).getStringCellValue().trim().isEmpty()
                        || !row.getCell(7).getStringCellValue().trim().isEmpty()
                        || !row.getCell(8).getStringCellValue().trim().isEmpty()
                        || !row.getCell(9).getStringCellValue().trim().isEmpty()
                        || !row.getCell(10).getStringCellValue().trim().isEmpty()
                        || !row.getCell(11).getStringCellValue().trim().isEmpty()
                        || !row.getCell(12).getStringCellValue().trim().isEmpty()
                        || !row.getCell(13).getStringCellValue().trim().isEmpty()
                        || !row.getCell(14).getStringCellValue().trim().isEmpty()
                        || !row.getCell(15).getStringCellValue().trim().isEmpty()
                        || !row.getCell(16).getStringCellValue().trim().isEmpty()
                        || !row.getCell(17).getStringCellValue().trim().isEmpty()
                        || !row.getCell(18).getStringCellValue().trim().isEmpty()
                        || !row.getCell(19).getStringCellValue().trim().isEmpty()
                        || !row.getCell(20).getStringCellValue().trim().isEmpty()
                        || !row.getCell(21).getStringCellValue().trim().isEmpty()
                        || !row.getCell(22).getStringCellValue().trim().isEmpty()
                        || !row.getCell(23).getStringCellValue().trim().isEmpty()
                        || !row.getCell(24).getStringCellValue().trim().isEmpty()
                        || !row.getCell(25).getStringCellValue().trim().isEmpty()
                        || !row.getCell(26).getStringCellValue().trim().isEmpty()
                        || !row.getCell(27).getStringCellValue().trim().isEmpty();

            case SMO:
                return     !row.getCell(0).getStringCellValue().trim().isEmpty()
                        || !row.getCell(5).getStringCellValue().trim().isEmpty()
                        || !row.getCell(6).getStringCellValue().trim().isEmpty()
                        || !row.getCell(7).getStringCellValue().trim().isEmpty()
                        || !row.getCell(8).getStringCellValue().trim().isEmpty()
                        || !row.getCell(9).getStringCellValue().trim().isEmpty()
                        || !row.getCell(10).getStringCellValue().trim().isEmpty()
                        || !row.getCell(11).getStringCellValue().trim().isEmpty()
                        || !row.getCell(12).getStringCellValue().trim().isEmpty()
                        || !row.getCell(13).getStringCellValue().trim().isEmpty()
                        || !row.getCell(14).getStringCellValue().trim().isEmpty()
                        || !row.getCell(15).getStringCellValue().trim().isEmpty()
                        || !row.getCell(16).getStringCellValue().trim().isEmpty()
                        || !row.getCell(17).getStringCellValue().trim().isEmpty()
                        || !row.getCell(18).getStringCellValue().trim().isEmpty()
                        || !row.getCell(19).getStringCellValue().trim().isEmpty()
                        || !row.getCell(20).getStringCellValue().trim().isEmpty()
                        || !row.getCell(21).getStringCellValue().trim().isEmpty()
                        || !row.getCell(22).getStringCellValue().trim().isEmpty();
            default:
                return false;
        }
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
