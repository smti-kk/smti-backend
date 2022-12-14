package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApFromExcelDTO {

    protected String npp;

    // organization.location.fias - Номер населенного пункта в ФИАС [6]
    protected String fiasLocation;

    // organization.name - Полное наименование учреждения [7]
    protected String name;

    // address & organization.address - Адрес точки подключения [8]
    protected String address;

    // point - Ширина [9]
    protected String latitude;

    // point - Долгота [10]
    protected String longitude;

    // organization.fias - Номер учреждения в ФИАС [11]
    protected String fias;

    // funCustomer - Функциональный заказчик [12]
    protected String functionalCustomer;

    // organization.smo - Вид СЗО [12] DELETE
//    private final String smo;

    // organization.type - Тип учреждения [13] DELETE
//    private final String type;

    // contractor - Оператор связи [14] DELETE
//    private final String contractor;

    // internetAccess - Тип подключения [13]
    protected String typeInternetAccess;

    // declaredSpeed - Скорость подключения [14]
    protected String declaredSpeed;

    // contractId - ID (по контракту) [ESPD: 15] [SMO: 16]
    protected String contractId;

    // contract - Контракт [ESPD: 25] [SMO: 17]
    protected String contract;

    // contacts - Контакты [ESPD: 17] [SMO: 18]
    protected String contacts;

    // change - Изменение [ESPD: 18] [SMO: 19]
    protected String changeType;

    // dateConnectionOrChange - Дата подключения/изменения [ESPD: 19] [SMO: 20]
    protected String connectionOrChangeDate;

    // numIncomingMessage - № вх. письма от ведомтсва [ESPD: 20] [SMO: 21]
    protected String nppIncomingMessage;

    // commentary - Комментарии [22]
    protected String comments;

    // deleted - Активность [ESPD: 28] [SMO: 23] (должно быть слово "нет" или ничего, нужно преобразовать в bool)
    protected String activity;

    // governmentDevelopmentProgram - Наименование программы [18] DELETE
//    private final String program;

    // //type\\ - Тип точки подключения [21]
//    private final String typeAccessPoint;

//    public ApFromExcelDTO(Row row) {
//        this(
//                row.getCell(0).getStringCellValue().trim(),
//                row.getCell(5).getStringCellValue().trim(),
//                row.getCell(6).getStringCellValue().trim(),
//                row.getCell(7).getStringCellValue().trim(),
//                row.getCell(8).getStringCellValue().trim().replaceAll(",", "."),
//                row.getCell(9).getStringCellValue().trim().replaceAll(",", "."),
//                row.getCell(10).getStringCellValue().trim(),
//                row.getCell(11).getStringCellValue().trim().toLowerCase().equals("нет") ?
//                    "" : row.getCell(11).getStringCellValue().trim(),
//                row.getCell(12).getStringCellValue().trim(),
//                row.getCell(13).getStringCellValue().trim(),
//                row.getCell(14).getStringCellValue().trim(),
//                row.getCell(15).getStringCellValue().trim(),
//                row.getCell(17).getStringCellValue().trim(),
//                row.getCell(19).getStringCellValue().trim()
//        );
//    }
}
