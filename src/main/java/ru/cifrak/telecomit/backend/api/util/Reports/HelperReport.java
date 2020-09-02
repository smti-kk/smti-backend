package ru.cifrak.telecomit.backend.api.util.Reports;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cifrak.telecomit.backend.api.dto.ExelReportAccessPointFullDTO;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.utils.export.ExcelExporter;
import ru.cifrak.telecomit.backend.utils.export.ExportToExcelConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class HelperReport {

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
