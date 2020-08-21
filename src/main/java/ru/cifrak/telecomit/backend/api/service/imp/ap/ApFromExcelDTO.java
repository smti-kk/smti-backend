package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class ApFromExcelDTO {

    private final String npp;

    private final String latitude;

    private final String longitude;

    private final String organization;

    private final String contractor;

    private final String typeInternetAccess;

    private final String declaredSpeed;

    public ApFromExcelDTO(Row row) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(8).getStringCellValue().trim(),
                row.getCell(9).getStringCellValue().trim(),
                row.getCell(10).getStringCellValue().trim(),
                row.getCell(13).getStringCellValue().trim(),
                row.getCell(14).getStringCellValue().trim(),
                row.getCell(15).getStringCellValue().trim()
        );
    }
}
