package ru.cifrak.telecomit.backend.api.service.imp.basestation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class BaseStationFromExcelDTO {

    private final String npp;

    private final String address;

    private final String latitude;

    private final String longitude;

    private final String operator;

    private final String mobileType;

    private final String coverageRadius;

    private final String propHeight;

    private final String actionDate;

    public BaseStationFromExcelDTO(Row row, String actionDate) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(1).getStringCellValue().trim(),
                row.getCell(2).getStringCellValue().trim().replaceAll(",", "."),
                row.getCell(3).getStringCellValue().trim().replaceAll(",", "."),
                row.getCell(4).getStringCellValue().trim(),
                row.getCell(5).getStringCellValue().trim(),
                row.getCell(6).getStringCellValue().trim(),
                row.getCell(7).getStringCellValue().trim(),
                actionDate.trim()
        );
    }
}
