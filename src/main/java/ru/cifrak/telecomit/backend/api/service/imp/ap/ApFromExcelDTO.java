package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@Getter
public class ApFromExcelDTO {

    private final String npp;

    // organization.location.fias - Номер населенного пункта в ФИАС [6]
    private final String fiasLocation;

    // organization.name - Полное наименование учреждения [7]
    private final String name;

    // address & organization.address - Адрес точки подключения [8]
    private final String address;

    // point - Ширина [9]
    private final String latitude;

    // point - Долгота [10]
    private final String longitude;

    // organization.fias - Номер учреждения в ФИАС [11]
    private final String fias;

    // organization.smo - Вид СЗО [12]
    private final String smo;

    // organization.type - Тип учреждения [13]
    private final String type;

    // contractor - Оператор связи [14]
    private final String contractor;

    // internetAccess - Тип подключения [15]
    private final String typeInternetAccess;

    // declaredSpeed - Скорость подключения [16]
    private final String declaredSpeed;

    // governmentDevelopmentProgram - Наименование программы [18]
    private final String program;

    // //type\\ - Тип точки подключения [21]
    private final String typeAccessPoint;

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
                row.getCell(15).getStringCellValue().trim(),
                row.getCell(17).getStringCellValue().trim(),
                row.getCell(19).getStringCellValue().trim()
        );
    }
}
