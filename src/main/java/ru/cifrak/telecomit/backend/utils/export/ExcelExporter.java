package ru.cifrak.telecomit.backend.utils.export;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ExcelExporter<T> {
    private ExportToExcelConfiguration<T> exportToExcelConfiguration;

    public ExcelExporter(ExportToExcelConfiguration<T> exportToExcelConfiguration) {
        this.exportToExcelConfiguration = exportToExcelConfiguration;
    }
    // TODO:1049: add for OutputStream
    public void exportToExcelFile(Collection<T> exportObjects, String file) throws IOException {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("1");

        setHeader(sheet, book);

        CellStyle cellStyle = getCellStyle(book);

        int i = 1;
        for (T object : exportObjects) {
            Row row = sheet.createRow(i);
            for (ColumnConfig columnConfig : exportToExcelConfiguration.getColumnConfigs()) {
                Cell cell = row.createCell(columnConfig.getColumnNumber());
                cell.setCellStyle(cellStyle);

                Object apply = columnConfig.getToTypeMethod().apply(object);

                if (apply == null) {
                    continue;
                }

                if (columnConfig.getClazz() == Date.class) {
                    DataFormat format = book.createDataFormat();
                    CellStyle dateCellStyle = getCellStyle(book);
                    dateCellStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
                    cell.setCellStyle(dateCellStyle);
                    cell.setCellValue((Date) apply);

                } else if (columnConfig.getClazz() == Calendar.class) {
                    cell.setCellValue((Calendar) apply);

                } else if (RichTextString.class.isAssignableFrom(columnConfig.getClazz())) {
                    cell.setCellValue((RichTextString) apply);

                } else if (Comment.class.isAssignableFrom(columnConfig.getClazz())) {
                    cell.setCellComment((Comment) apply);

                } else if (Hyperlink.class.isAssignableFrom(columnConfig.getClazz())) {
                    cell.setHyperlink((Hyperlink) apply);

                } else if (columnConfig.getClazz() == Double.class) {
                    cell.setCellValue((Double) apply);

                } else {
                    cell.setCellValue(apply.toString());
                }
            }
            i++;
        }

        autoSizeColumns(sheet);
        book.write(new FileOutputStream(file));
        book.close();
    }


    private void autoSizeColumns(Sheet sheet) {
        exportToExcelConfiguration.getColumnConfigs()
                .forEach(columnConfig -> sheet.autoSizeColumn(columnConfig.getColumnNumber()));
    }

    private CellStyle getCellStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.HAIR);
        cellStyle.setBorderTop(BorderStyle.HAIR);
        cellStyle.setBorderRight(BorderStyle.HAIR);
        cellStyle.setBorderLeft(BorderStyle.HAIR);
        return cellStyle;
    }

    private void setHeader(Sheet sheet, Workbook workbook) {
        Row row = sheet.createRow(0);

        CellStyle bold = getCellStyle(workbook);
        Font font = workbook.createFont();
        font.setBold(true);
        bold.setFont(font);
        exportToExcelConfiguration.getColumnConfigs().forEach(columnConfig -> {
            Cell cell = row.createCell(columnConfig.getColumnNumber());
            cell.setCellStyle(bold);
            cell.setCellValue(columnConfig.getHeader());
        });
    }

    public byte[] exportToByteArray(Collection<T> exportObjects) throws IOException {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("1");

        setHeader(sheet, book);

        CellStyle cellStyle = getCellStyle(book);

        int i = 1;
        for (T object : exportObjects) {
            Row row = sheet.createRow(i);
            for (ColumnConfig columnConfig : exportToExcelConfiguration.getColumnConfigs()) {
                Cell cell = row.createCell(columnConfig.getColumnNumber());
                cell.setCellStyle(cellStyle);

                Object apply = columnConfig.getToTypeMethod().apply(object);

                if (apply == null) {
                    continue;
                }

                if (columnConfig.getClazz() == Date.class) {
                    DataFormat format = book.createDataFormat();
                    CellStyle dateCellStyle = getCellStyle(book);
                    dateCellStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
                    cell.setCellStyle(dateCellStyle);
                    cell.setCellValue((Date) apply);

                } else if (columnConfig.getClazz() == Calendar.class) {
                    cell.setCellValue((Calendar) apply);

                } else if (RichTextString.class.isAssignableFrom(columnConfig.getClazz())) {
                    cell.setCellValue((RichTextString) apply);

                } else if (Comment.class.isAssignableFrom(columnConfig.getClazz())) {
                    cell.setCellComment((Comment) apply);

                } else if (Hyperlink.class.isAssignableFrom(columnConfig.getClazz())) {
                    cell.setHyperlink((Hyperlink) apply);

                } else if (columnConfig.getClazz() == Double.class) {
                    cell.setCellValue((Double) apply);

                } else {
                    cell.setCellValue(apply.toString());
                }
            }
            i++;
        }

        autoSizeColumns(sheet);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        book.write(baos);
        book.close();
        return baos.toByteArray();
    }
}

