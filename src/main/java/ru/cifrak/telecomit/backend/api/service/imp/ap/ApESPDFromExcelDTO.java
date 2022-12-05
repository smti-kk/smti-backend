package ru.cifrak.telecomit.backend.api.service.imp.ap;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

@Getter
public class ApESPDFromExcelDTO extends ApFromExcelDTO {

    // espdWhiteIp - Белый IP ЕСПД [16]
    protected String espdWhiteIp;

    // numSourceEmailsRTK - № исх.письма на РТК [21]
    protected String nppSourceMessageRTK;

    // oneTimePay - Разовый, руб. с НДС [23]
    protected String oneTimePay;

    // monthlyPay - Ежемес, руб. с НДС [24]
    protected String monthlyPay;

    // zspdWhiteIp - Белый IP ЗСПД [26]
    protected String zspdWhiteIp;

    // availZspdOrMethodConToZspd - Наличие ЗСПД/ способ подключения к ЗСПД [27]
    protected String availZSPD;

    public ApESPDFromExcelDTO(Row row) {
        super(
                row.getCell(0).getStringCellValue().trim(),  // npp - №
                row.getCell(5).getStringCellValue().trim(),  // fiasLocation - Номер населенного пункта в ФИАС
                row.getCell(6).getStringCellValue().trim(),  // name - Полное наименование учреждения
                row.getCell(7).getStringCellValue().trim(),  // address - Адрес точки подключения
                row.getCell(8).getStringCellValue().trim()   // latitude - Ширина
                        .replaceAll(",", "."),
                row.getCell(9).getStringCellValue().trim()   // longitude - Долгота
                        .replaceAll(",", "."),
                row.getCell(10).getStringCellValue().trim(), // fias - Номер учреждения в ФИАС
                row.getCell(11).getStringCellValue().trim(), // functionalCustomer - Функциональный заказчик
                row.getCell(12).getStringCellValue().trim(), // typeInternetAccess - Тип подключения
                row.getCell(13).getStringCellValue().trim(), // declaredSpeed - Скорость подключения
                row.getCell(14).getStringCellValue().trim(), // contractId - ID (по контракту)
                row.getCell(24).getStringCellValue().trim(), // contract - Контракт
                row.getCell(16).getStringCellValue().trim(), // contacts - Контакты
                row.getCell(17).getStringCellValue().trim(), // changeType - Изменение
                row.getCell(18).getStringCellValue().trim(), // connectionOrChangeDate - Дата подключения/изменения
                row.getCell(19).getStringCellValue().trim(), // nppIncomingMessage - № вх. письма от ведомтсва
                row.getCell(21).getStringCellValue().trim(), // comments - Комментарии
                row.getCell(27).getStringCellValue().trim()  // activity - Активность
        );

        this.espdWhiteIp =         row.getCell(15).getStringCellValue().trim();
        this.nppSourceMessageRTK = row.getCell(20).getStringCellValue().trim();
        this.oneTimePay =          row.getCell(22).getStringCellValue().trim();
        this.monthlyPay =          row.getCell(23).getStringCellValue().trim();
        this.zspdWhiteIp =         row.getCell(25).getStringCellValue().trim();
        this.availZSPD =           row.getCell(26).getStringCellValue().trim();
    }

}
