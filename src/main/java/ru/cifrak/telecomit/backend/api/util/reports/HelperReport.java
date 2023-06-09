package ru.cifrak.telecomit.backend.api.util.reports;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.api.dto.ExelReportAccessPointFullDTO;
import ru.cifrak.telecomit.backend.api.dto.ExelReportLocation;
import ru.cifrak.telecomit.backend.api.dto.ExelReportLocationWithoutLogged;
import ru.cifrak.telecomit.backend.api.dto.FeatureExportDTO;
import ru.cifrak.telecomit.backend.entities.TcType;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.utils.export.ExcelExporter;
import ru.cifrak.telecomit.backend.utils.export.ExportToExcelConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HelperReport {

    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static ExcelExporter<ExelReportAccessPointFullDTO> generateExelFormat () {
        ExportToExcelConfiguration<ExelReportAccessPointFullDTO> exportToExcelConfiguration = new ExportToExcelConfiguration<>();
        exportToExcelConfiguration.addColumn(0, Integer.class, ExelReportAccessPointFullDTO::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, Integer.class, ExelReportAccessPointFullDTO::getIdOrg, "ID учреждения");
        exportToExcelConfiguration.addColumn(2, ExelReportAccessPointFullDTO::getMunicipalLocationType, "Вид муниципального образования");
        exportToExcelConfiguration.addColumn(3, ExelReportAccessPointFullDTO::getMunicipalName, "Муниципальное образование");
        exportToExcelConfiguration.addColumn(4, ExelReportAccessPointFullDTO::getLocationType, "Тип населенного пункта");
        exportToExcelConfiguration.addColumn(5, ExelReportAccessPointFullDTO::getLocationName, "Наименование населенного пункта");
        exportToExcelConfiguration.addColumn(6, ExelReportAccessPointFullDTO::getOKTMO, "ОКТМО");
        exportToExcelConfiguration.addColumn(7, Integer.class, ExelReportAccessPointFullDTO::getNumberInhabitants, "Численность населения (данные Росстата)");
        exportToExcelConfiguration.addColumn(8, ExelReportAccessPointFullDTO::getFullNameOrganization, "Полное наименование учреждения");
        exportToExcelConfiguration.addColumn(9, ExelReportAccessPointFullDTO::getFIASOrganization, "Номер учреждения в ФИАС");
        exportToExcelConfiguration.addColumn(10, ExelReportAccessPointFullDTO::getFullAddressOrganization, "Адрес учреждения");
        exportToExcelConfiguration.addColumn(11, Double.class,ExelReportAccessPointFullDTO::getLatitude, "Широта");
        exportToExcelConfiguration.addColumn(12, Double.class,ExelReportAccessPointFullDTO::getLongitude, "Долгота");
        exportToExcelConfiguration.addColumn(13, ExelReportAccessPointFullDTO::getSMO, "Вид СЗО");
        exportToExcelConfiguration.addColumn(14, ExelReportAccessPointFullDTO::getCompanyType, "Тип учреждения");
        exportToExcelConfiguration.addColumn(15, Boolean.class,ExelReportAccessPointFullDTO::getPointView, "Отображается");
        exportToExcelConfiguration.addColumn(16, ExelReportAccessPointFullDTO::getAccessPointCustomer, "Заказчик");
        exportToExcelConfiguration.addColumn(17, ExelReportAccessPointFullDTO::getContract, "Контракт");
        //кто это такой
        exportToExcelConfiguration.addColumn(18, Integer.class,ExelReportAccessPointFullDTO::getUcn, "Название узла согласно кодификатору");
        exportToExcelConfiguration.addColumn(19, ExelReportAccessPointFullDTO::getAccessNode, "Узел доступа (Идентификатор узла для работы мониторинга)");
        exportToExcelConfiguration.addColumn(20, ExelReportAccessPointFullDTO::getDescriptionAccess, "Описание");
        exportToExcelConfiguration.addColumn(21, ExelReportAccessPointFullDTO::getIncludeType, "Тип подключения");
        exportToExcelConfiguration.addColumn(22, ExelReportAccessPointFullDTO::getOperatorName, "Оператор связи");
        exportToExcelConfiguration.addColumn(23, ExelReportAccessPointFullDTO::getDeclaredSpeed, "Скорость по контракту (Мбит/с)");
        exportToExcelConfiguration.addColumn(24, ExelReportAccessPointFullDTO::getChannelWidth, "Ширина канала (Мбит/с)");
        exportToExcelConfiguration.addColumn(25, ExelReportAccessPointFullDTO::getCommunicationAssessment, "Качество связи");
        exportToExcelConfiguration.addColumn(26, Integer.class, ExelReportAccessPointFullDTO::getUcn, "Уникальный номер по контракту (ЕСПД, СЗО)");
        exportToExcelConfiguration.addColumn(27, ExelReportAccessPointFullDTO::getGovernmentProgramName, "Государственная программа");
        exportToExcelConfiguration.addColumn(28, ExelReportAccessPointFullDTO::getParticipationStatus, "Статус участия");
        exportToExcelConfiguration.addColumn(29, Integer.class, ExelReportAccessPointFullDTO::getYearOverGovProgram, "Год реализации");
        return new ExcelExporter<>(exportToExcelConfiguration);
    }

    public static ExcelExporter<ExelReportAccessPointFullDTO> generateExelFormat (User user) {
        ExportToExcelConfiguration<ExelReportAccessPointFullDTO> exportToExcelConfiguration = new ExportToExcelConfiguration<>();
        exportToExcelConfiguration.addColumn(0, Integer.class, ExelReportAccessPointFullDTO::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, Integer.class, ExelReportAccessPointFullDTO::getIdOrg, "ID учреждения");
        exportToExcelConfiguration.addColumn(2, ExelReportAccessPointFullDTO::getMunicipalLocationType, "Вид муниципального образования");
        exportToExcelConfiguration.addColumn(3, ExelReportAccessPointFullDTO::getMunicipalName, "Муниципальное образование");
        exportToExcelConfiguration.addColumn(4, ExelReportAccessPointFullDTO::getLocationType, "Тип населенного пункта");
        exportToExcelConfiguration.addColumn(5, ExelReportAccessPointFullDTO::getLocationName, "Наименование населенного пункта");
        exportToExcelConfiguration.addColumn(6, ExelReportAccessPointFullDTO::getOKTMO, "ОКТМО");
        exportToExcelConfiguration.addColumn(7, Integer.class, ExelReportAccessPointFullDTO::getNumberInhabitants, "Численность населения (данные Росстата)");
        exportToExcelConfiguration.addColumn(8, ExelReportAccessPointFullDTO::getFullNameOrganization, "Полное наименование учреждения");
        exportToExcelConfiguration.addColumn(9, ExelReportAccessPointFullDTO::getFIASOrganization, "Номер учреждения в ФИАС");
        exportToExcelConfiguration.addColumn(10, ExelReportAccessPointFullDTO::getFullAddressOrganization, "Адрес учреждения");
        exportToExcelConfiguration.addColumn(11, Double.class,ExelReportAccessPointFullDTO::getLatitude, "Широта");
        exportToExcelConfiguration.addColumn(12, Double.class,ExelReportAccessPointFullDTO::getLongitude, "Долгота");
        exportToExcelConfiguration.addColumn(13, ExelReportAccessPointFullDTO::getFunCustomer, "Функциональный заказчик");
        exportToExcelConfiguration.addColumn(14, ExelReportAccessPointFullDTO::getCompanyType, "Тип учреждения");
        exportToExcelConfiguration.addColumn(15, Boolean.class,ExelReportAccessPointFullDTO::getPointView, "Отображается");
        exportToExcelConfiguration.addColumn(16, ExelReportAccessPointFullDTO::getAccessPointCustomer, "Заказчик");
        exportToExcelConfiguration.addColumn(17, ExelReportAccessPointFullDTO::getContract, "Контракт");
        //кто это такой
        exportToExcelConfiguration.addColumn(18, Integer.class,ExelReportAccessPointFullDTO::getUcn, "Название узла согласно кодификатору");
        exportToExcelConfiguration.addColumn(19, ExelReportAccessPointFullDTO::getAccessNode, "Узел доступа (Идентификатор узла для работы мониторинга)");
        exportToExcelConfiguration.addColumn(20, ExelReportAccessPointFullDTO::getDescriptionAccess, "Описание");
        exportToExcelConfiguration.addColumn(21, ExelReportAccessPointFullDTO::getIncludeType, "Тип подключения");
        exportToExcelConfiguration.addColumn(22, ExelReportAccessPointFullDTO::getOperatorName, "Оператор связи");
        exportToExcelConfiguration.addColumn(23, ExelReportAccessPointFullDTO::getDeclaredSpeed, "Скорость по контракту (Мбит/с)");
        exportToExcelConfiguration.addColumn(24, ExelReportAccessPointFullDTO::getChannelWidth, "Ширина канала (Мбит/с)");
        exportToExcelConfiguration.addColumn(25, ExelReportAccessPointFullDTO::getCommunicationAssessment, "Качество связи");
        exportToExcelConfiguration.addColumn(26, Integer.class, ExelReportAccessPointFullDTO::getUcn, "Уникальный номер по контракту (ЕСПД, СЗО)");
        exportToExcelConfiguration.addColumn(27, ExelReportAccessPointFullDTO::getGovernmentProgramName, "Государственная программа");
        exportToExcelConfiguration.addColumn(28, ExelReportAccessPointFullDTO::getParticipationStatus, "Статус участия");
        exportToExcelConfiguration.addColumn(29, Integer.class, ExelReportAccessPointFullDTO::getYearOverGovProgram, "Год реализации");

        if (user.getRoles().contains(UserRole.CONTRACTOR)) {
            exportToExcelConfiguration.addColumn(30, Date.class, ExelReportAccessPointFullDTO::getCreateDate, "Дата постановки на мониторинг");
            exportToExcelConfiguration.addColumn(31, Integer.class, ExelReportAccessPointFullDTO::getMonitoring, "Мониторинг");
            exportToExcelConfiguration.addColumn(32, Integer.class, ExelReportAccessPointFullDTO::getConnectionState, "Статус");
            exportToExcelConfiguration.addColumn(33, Integer.class, ExelReportAccessPointFullDTO::getProblem, "Проблема");
            exportToExcelConfiguration.addColumn(34, Integer.class, ExelReportAccessPointFullDTO::getImportance, "Важность");
            exportToExcelConfiguration.addColumn(35, Integer.class, ExelReportAccessPointFullDTO::getProblemDefinition, "Описание проблемы");
            exportToExcelConfiguration.addColumn(36, Integer.class, ExelReportAccessPointFullDTO::getDayTraffic, "Трафик");

            exportToExcelConfiguration.addColumn(37, Integer.class, ExelReportAccessPointFullDTO::getContractId, "ID (по контракту)");
            exportToExcelConfiguration.addColumn(38, ExelReportAccessPointFullDTO::getContract, "Контракт");
            exportToExcelConfiguration.addColumn(39, ExelReportAccessPointFullDTO::getContacts, "Контакты");
            exportToExcelConfiguration.addColumn(40, ExelReportAccessPointFullDTO::getChange, "Изменение");
            exportToExcelConfiguration.addColumn(41, LocalDate.class, ExelReportAccessPointFullDTO::getDateConnectionOrChange, "Дата подключения/ изменения");
            exportToExcelConfiguration.addColumn(42, ExelReportAccessPointFullDTO::getNumIncomingMessage, "№ вх.письма от ведомства");
            exportToExcelConfiguration.addColumn(43, ExelReportAccessPointFullDTO::getCommentary, "Комментарии");
            exportToExcelConfiguration.addColumn(44, ExelReportAccessPointFullDTO::getEspdWhiteIp, "Белый IP ЕСПД");
            exportToExcelConfiguration.addColumn(45, ExelReportAccessPointFullDTO::getNumSourceEmailsRTK, "№ исх. письма на РТК");
            exportToExcelConfiguration.addColumn(46, BigDecimal.class, ExelReportAccessPointFullDTO::getOneTimePay, "Разовый, руб. с НДС");
            exportToExcelConfiguration.addColumn(47, BigDecimal.class, ExelReportAccessPointFullDTO::getMonthlyPay, "Ежемес, руб. с НДС");
            exportToExcelConfiguration.addColumn(48, ExelReportAccessPointFullDTO::getZspdWhiteIp, "Белый IP ЗСПД");
            exportToExcelConfiguration.addColumn(49, ExelReportAccessPointFullDTO::getAvailZspdOrMethodConToZspd, "Наличие ЗСПД/ способ подключения к ЗСПД");
            exportToExcelConfiguration.addColumn(50, LocalDate.class, ExelReportAccessPointFullDTO::getDateCommissioning, "Дата ввода в эксплуатацию");
        } else {
            exportToExcelConfiguration.addColumn(30, Integer.class, ExelReportAccessPointFullDTO::getContractId, "ID (по контракту)");
            exportToExcelConfiguration.addColumn(31, ExelReportAccessPointFullDTO::getContract, "Контракт");
            exportToExcelConfiguration.addColumn(32, ExelReportAccessPointFullDTO::getContacts, "Контакты");
            exportToExcelConfiguration.addColumn(33, ExelReportAccessPointFullDTO::getChange, "Изменение");
            exportToExcelConfiguration.addColumn(34, LocalDate.class, ExelReportAccessPointFullDTO::getDateConnectionOrChange, "Дата подключения/ изменения");
            exportToExcelConfiguration.addColumn(35, ExelReportAccessPointFullDTO::getNumIncomingMessage, "№ вх.письма от ведомства");
            exportToExcelConfiguration.addColumn(36, ExelReportAccessPointFullDTO::getCommentary, "Комментарии");
            exportToExcelConfiguration.addColumn(37, ExelReportAccessPointFullDTO::getEspdWhiteIp, "Белый IP ЕСПД");
            exportToExcelConfiguration.addColumn(38, ExelReportAccessPointFullDTO::getNumSourceEmailsRTK, "№ исх. письма на РТК");
            exportToExcelConfiguration.addColumn(39, BigDecimal.class, ExelReportAccessPointFullDTO::getOneTimePay, "Разовый, руб. с НДС");
            exportToExcelConfiguration.addColumn(40, BigDecimal.class, ExelReportAccessPointFullDTO::getMonthlyPay, "Ежемес, руб. с НДС");
            exportToExcelConfiguration.addColumn(41, ExelReportAccessPointFullDTO::getZspdWhiteIp, "Белый IP ЗСПД");
            exportToExcelConfiguration.addColumn(42, ExelReportAccessPointFullDTO::getAvailZspdOrMethodConToZspd, "Наличие ЗСПД/ способ подключения к ЗСПД");
            exportToExcelConfiguration.addColumn(43, LocalDate.class, ExelReportAccessPointFullDTO::getDateCommissioning, "Дата ввода в эксплуатацию");
        }

        return new ExcelExporter<>(exportToExcelConfiguration);
    }

    public static ExcelExporter<ExelReportLocation> generateExelFormatLocationType () {

        ExportToExcelConfiguration<ExelReportLocation> exportToExcelConfiguration = new ExportToExcelConfiguration<>();

        exportToExcelConfiguration.addColumn(0, Integer.class, ExelReportLocation::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, ExelReportLocation::getDistrictName, "Муниципальное образование");
        exportToExcelConfiguration.addColumn(2, ExelReportLocation::getDistrict, "Тип МО");
        exportToExcelConfiguration.addColumn(3, ExelReportLocation::getLocationName, "Населенный пункт");
        exportToExcelConfiguration.addColumn(4, ExelReportLocation::getLocationType, "Тип населенного пункта");
        exportToExcelConfiguration.addColumn(5, Integer.class,ExelReportLocation::getPopulation, "Население");
        exportToExcelConfiguration.addColumn(6, ExelReportLocation::getCellular, "Телефон");
        exportToExcelConfiguration.addColumn(7, ExelReportLocation::getPayphone, "Таксофон (кол-во)");
        exportToExcelConfiguration.addColumn(8,Boolean.class, ExelReportLocation::getInfomat, "Инфомат");
        exportToExcelConfiguration.addColumn(9, ExelReportLocation::getPost, "Почта");
        exportToExcelConfiguration.addColumn(10, ExelReportLocation::getTV, "Телевидение");
        exportToExcelConfiguration.addColumn(11, ExelReportLocation::getRadio, "Радио");
        exportToExcelConfiguration.addColumn(12, ExelReportLocation::getTelephone, "Сотовая связь");
        exportToExcelConfiguration.addColumn(13, ExelReportLocation::getInternet, "Интернет");
        exportToExcelConfiguration.addColumn(14, ExelReportLocation::getFias, "ФИАС");
        exportToExcelConfiguration.addColumn(15, ExelReportLocation::getProgram, "Программа");

        return new ExcelExporter<>(exportToExcelConfiguration);
    }

    //ToDo: такая же, но только с интернетом
    // Может быть, получится как либо общить.
    public static ExcelExporter<FeatureExportDTO> generateExelFeatureReport (TcType tcType) {
        ExportToExcelConfiguration<FeatureExportDTO> exportToExcelConfiguration = new ExportToExcelConfiguration<>();

        exportToExcelConfiguration.addColumn(0, Integer.class, FeatureExportDTO::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, FeatureExportDTO::getDistrictName, "Район");
        exportToExcelConfiguration.addColumn(2, FeatureExportDTO::getDistrict, "Тип МО");
        exportToExcelConfiguration.addColumn(3, FeatureExportDTO::getLocationName, "Населенный пункт");
        exportToExcelConfiguration.addColumn(4, FeatureExportDTO::getLocationName, "Тип населенного пункта");
        exportToExcelConfiguration.addColumn(5, Integer.class,FeatureExportDTO::getPopulation, "Население");

        exportToExcelConfiguration.addColumn(6, FeatureExportDTO::getActual, tcType.toString() + "(сейчас" + currentYear + ")");
        exportToExcelConfiguration.addColumn(7, FeatureExportDTO::getPlanForOneYear, tcType.toString() + "(план" + currentYear + 1 + ")");
        exportToExcelConfiguration.addColumn(8, FeatureExportDTO::getPlanForTwoYear, tcType.toString() + "(план" + currentYear + 2 + ")");
        exportToExcelConfiguration.addColumn(9, FeatureExportDTO::getArchive, tcType.toString() + "(архив)");

        return new ExcelExporter<>(exportToExcelConfiguration);
    }


    public static Sort.Order getOrder(String value) {

        if (StringUtils.startsWith(value, "-")) {
            return new Sort.Order(Sort.Direction.DESC, StringUtils.substringAfter(value, "-"));
        } else if (StringUtils.startsWith(value, "+")) {
            return new Sort.Order(Sort.Direction.ASC, StringUtils.substringAfter(value, "+"));
        } else {
            // Sometimes '+' from query param can be replaced as ' '
            return new Sort.Order(Sort.Direction.ASC, StringUtils.trim(value));
        }

    }

    @Nullable
    public static Sort getSortRule(@RequestParam(name = "sort", required = false) String sort) {
        Set<String> sortingFileds = new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils
                        .defaultIfEmpty(sort, ""), ",")));

        List<Sort.Order> sortingOrders = sortingFileds.stream().map(HelperReport::getOrder)
                .collect(Collectors.toList());

        return sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);
    }

}
