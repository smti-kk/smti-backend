package ru.cifrak.telecomit.backend.api.service.imp.tcats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class TcAtsFromExcelDTO {

    private final String npp;

    private final String fias;

    private final String operator;

    private final String activity;

    public TcAtsFromExcelDTO(Row row) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(5).getStringCellValue().trim(),
                row.getCell(6).getStringCellValue().trim(),
                row.getCell(10).getStringCellValue().trim()
        );
    }
}
