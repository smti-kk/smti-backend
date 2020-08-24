package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class ApFromExcelDTO {

    private final String npp;

    private final String fiasLocation;

    private final String name;

    private final String address;

    private final String latitude;

    private final String longitude;

    private final String fias;

    private final String smo;

    private final String type;

    private final String contractor;

    private final String typeInternetAccess;

    private final String declaredSpeed;

    public ApFromExcelDTO(Row row) {
        this(
                row.getCell(0).getStringCellValue().trim(),
                row.getCell(5).getStringCellValue().trim(),
                row.getCell(6).getStringCellValue().trim(),
                row.getCell(7).getStringCellValue().trim(),
                row.getCell(8).getStringCellValue().trim().replaceAll(",", "."),
                row.getCell(9).getStringCellValue().trim().replaceAll(",", "."),
                row.getCell(10).getStringCellValue().trim(),
                row.getCell(11).getStringCellValue().trim().toLowerCase().equals("нет") ?
                    "" : row.getCell(11).getStringCellValue().trim(),
                row.getCell(12).getStringCellValue().trim(),
                row.getCell(13).getStringCellValue().trim(),
                row.getCell(14).getStringCellValue().trim(),
                row.getCell(15).getStringCellValue().trim()
        );
    }
}
