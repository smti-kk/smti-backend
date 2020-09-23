package ru.cifrak.telecomit.backend.api.service.imp.trunkchannel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class TrunkChannelFromExcelDTO {

    private final String npp;

    private final String locationStart;

    private final String locationEnd;

    private final String operator;

    private final String type;

    private final String comissioning;

    private final String program;

    public TrunkChannelFromExcelDTO(Row row, String commissioning) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(5).getStringCellValue().trim(),
                row.getCell(10).getStringCellValue().trim(),
                row.getCell(11).getStringCellValue().trim(),
                row.getCell(12).getStringCellValue().trim(),
                commissioning.trim(),
                row.getCell(14).getStringCellValue().trim()
        );
    }
}
