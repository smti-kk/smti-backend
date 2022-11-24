package ru.cifrak.telecomit.backend.api.service.imp.tcpost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class TcPostFromExcelDTO {

    private final String npp;

    private final String fias;

    private final String operator;

    private final String typePost;

    private final String activity;

    public TcPostFromExcelDTO(Row row) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(5).getStringCellValue().trim(),
                row.getCell(6).getStringCellValue().trim(),
                row.getCell(7).getStringCellValue().trim(),
                row.getCell(11).getStringCellValue().trim()
        );
    }
}
