package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@Getter
public class ApSMOFromExcelDTO extends ApFromExcelDTO {

    // dataCommissioning - Дата ввода в эксплуатацию [15]
    protected String commissioningDate;

    public ApSMOFromExcelDTO(Row row) {
        super(
                row.getCell(0).getStringCellValue().trim(),  // npp - №
                row.getCell(5).getStringCellValue().trim(),  // fiasLocation - Номер населенного пункта в ФИАС
                row.getCell(6).getStringCellValue().trim(),  // name - Полное наименование учреждения
                row.getCell(7).getStringCellValue().trim(),  // address - Адрес точки подключения
                row.getCell(8).getStringCellValue().trim(),  // latitude - Ширина
                row.getCell(9).getStringCellValue().trim(),  // longitude - Долгота
                row.getCell(10).getStringCellValue().trim(), // fias - Номер учреждения в ФИАС
                row.getCell(11).getStringCellValue().trim(), // functionalCustomer - Функциональный заказчик
                row.getCell(12).getStringCellValue().trim(), // typeInternetAccess - Тип подключения
                row.getCell(13).getStringCellValue().trim(), // declaredSpeed - Скорость подключения
                row.getCell(15).getStringCellValue().trim(), // contractId - ID (по контракту)
                row.getCell(16).getStringCellValue().trim(), // contract - Контракт
                row.getCell(17).getStringCellValue().trim(), // contacts - Контакты
                row.getCell(18).getStringCellValue().trim(), // changeType - Изменение
                row.getCell(19).getStringCellValue().trim(), // connectionDate - Дата подключения/изменения
                row.getCell(20).getStringCellValue().trim(), // nppIncomingMessage - № вх. письма от ведомтсва
                row.getCell(21).getStringCellValue().trim(), // comments - Комментарии
                row.getCell(22).getStringCellValue().trim()  // activity - Активность
        );
        this.commissioningDate = row.getCell(14).getStringCellValue().trim();
    }

}
