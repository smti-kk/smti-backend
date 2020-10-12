package ru.cifrak.telecomit.backend.api.util.Reports;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.*;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPointsFull;
import ru.cifrak.telecomit.backend.repository.RepositoryApContract;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.specs.SpecificationAccessPointFull;
import ru.cifrak.telecomit.backend.service.ReportName;
import ru.cifrak.telecomit.backend.service.ServiceExternalReports;
import ru.cifrak.telecomit.backend.utils.Converter;
import ru.cifrak.telecomit.backend.utils.export.ExcelExporter;
import ru.cifrak.telecomit.backend.utils.export.ExportToExcelConfiguration;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.cifrak.telecomit.backend.api.util.Reports.HelperReport.generateExelFormat;

@Slf4j

@RestController
@RequestMapping("/api/report/organization")
public class ApiReports {
    private final RepositoryLocation rLocation;
    private final RepositoryAccessPointsFull rAccessPoints;
    private final RepositoryApContract rApContract;
    private final ServiceExternalReports serviceExternalReports;


    @Autowired
    public ApiReports(RepositoryLocation repository, RepositoryLocation rLocation, RepositoryAccessPointsFull rAccessPoints, RepositoryApContract rApContract, ServiceExternalReports serviceExternalReports) {
        this.rLocation = rLocation;
        this.rAccessPoints = rAccessPoints;
        this.rApContract = rApContract;
        this.serviceExternalReports = serviceExternalReports;
    }

    @GetMapping("/ap-all/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public PaginatedList<ReportAccessPointFullDTO> reportAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "location", required = false) Location location,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp,
            @RequestParam(name = "inet", required = false) TypeInternetAccess inettype,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "contractor", required = false) String contractor,
            @RequestParam(name = "population-start", required = false) Integer pStart,
            @RequestParam(name = "population-end", required = false) Integer pEnd,
            @RequestParam(name = "ap", required = false) List<TypeAccessPoint> ap,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        log.info("->GET /api/report/organization/[page={}, size={}, location={}, type={}, smo={}, gdp={}, inet={}, parents=xx, orgname={}, operator={} ]",
                page, size, location == null ? "" : location.getId(), type, smo, gdp, inettype, organization, contractor);
        //HINT: https://github.com/vijjayy81/spring-boot-jpa-rest-demo-filter-paging-sorting
        Set<String> sortingFileds = new LinkedHashSet<>(
                Arrays.asList(
                        StringUtils.split(StringUtils.defaultIfEmpty(sort, ""), ",")
                )
        );
        List<Sort.Order> sortingOrders = sortingFileds
                .stream()
                .map(HelperReport::getOrder)
                .collect(Collectors.toList());
        Sort sortData = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);
        Pageable pageConfig;
        if (sortData != null) {
            pageConfig = PageRequest.of(page - 1, size, sortData);
        } else {
            pageConfig = PageRequest.of(page - 1, size);
        }
        Specification<AccessPointFull> spec = Specification.where(null);
        if (location != null) {
            spec = spec != null ? spec.and(SpecificationAccessPointFull.inLocation(location)) : null;
        }
        if (type != null) {
            spec = spec.and(SpecificationAccessPointFull.withType(type));
        }
        if (smo != null) {
            spec = spec.and(SpecificationAccessPointFull.withSmo(smo));
        }
        if (gdp != null) {
            spec = spec.and(SpecificationAccessPointFull.withGovProgram(gdp));
        }
        if (inettype != null) {
            spec = spec.and(SpecificationAccessPointFull.withInetType(inettype));
        }
        if (parents != null) {
            spec = spec.and(SpecificationAccessPointFull.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(SpecificationAccessPointFull.withOrgname(organization));
        }
        if (contractor != null) {
            spec = spec.and(SpecificationAccessPointFull.withOperator(contractor));
        }
        if (pStart != null) {
            spec = spec.and(SpecificationAccessPointFull.pStart(pStart));
        }
        if (pEnd != null) {
            spec = spec.and(SpecificationAccessPointFull.pEnd(pEnd));
        }
        if (ap != null) {
            spec = spec.and(SpecificationAccessPointFull.type(ap));
        }
        Page<AccessPointFull> pageDatas = rAccessPoints.findAll(spec, pageConfig);
        PaginatedList<ReportAccessPointFullDTO> pList = new PaginatedList<>(
                pageDatas.getTotalElements(),
                pageDatas.stream()
                        .map(ReportAccessPointFullDTO::new)
                        .collect(Collectors.toList())
        );
        log.info("<-GET /api/report/organization/");
        return pList;
    }

    @GetMapping("/ap-contract/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public PaginatedList<ReportApContractDTO> reportContracts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "location", required = false) Location location,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp,
            @RequestParam(name = "inet", required = false) TypeInternetAccess inettype,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "contractor", required = false) String contractor,
            @RequestParam(name = "population-start", required = false) Integer pStart,
            @RequestParam(name = "population-end", required = false) Integer pEnd,
            @RequestParam(name = "contract", required = false) String contract,
            @RequestParam(name = "contract-start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cStart,
            @RequestParam(name = "contract-end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cEnd,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        log.info("->GET /api/report/contract/[page={}, size={}, location={}, type={}, smo={}, gdp={}, inet={}, parents=xx, orgname={}, operator={} ]",
                page, size, location == null ? "" : location.getId(), type, smo, gdp, inettype, organization, contractor);
        //HINT: https://github.com/vijjayy81/spring-boot-jpa-rest-demo-filter-paging-sorting
        Set<String> sortingFileds = new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils
                        .defaultIfEmpty(sort, ""), ",")));

        List<Sort.Order> sortingOrders = sortingFileds.stream().map(HelperReport::getOrder)
                .collect(Collectors.toList());

        Sort sortData = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);
        Pageable pageConfig;
        if (sortData != null) {
            pageConfig = PageRequest.of(page - 1, size, sortData);
        } else {
            pageConfig = PageRequest.of(page - 1, size);
        }
        Specification<AccessPointFull> spec = Specification.where(SpecificationAccessPointFull.apcontract());
        if (location != null) {
            spec = spec.and(SpecificationAccessPointFull.inLocation(location));
        }
        if (type != null) {
            spec = spec.and(SpecificationAccessPointFull.withType(type));
        }
        if (smo != null) {
            spec = spec.and(SpecificationAccessPointFull.withSmo(smo));
        }
        if (gdp != null) {
            spec = spec.and(SpecificationAccessPointFull.withGovProgram(gdp));
        }
        if (inettype != null) {
            spec = spec.and(SpecificationAccessPointFull.withInetType(inettype));
        }
        if (parents != null) {
            spec = spec.and(SpecificationAccessPointFull.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(SpecificationAccessPointFull.withOrgname(organization));
        }
        if (contractor != null) {
            spec = spec.and(SpecificationAccessPointFull.withOperator(contractor));
        }
        if (pStart != null) {
            spec = spec.and(SpecificationAccessPointFull.pStart(pStart));
        }
        if (pEnd != null) {
            spec = spec.and(SpecificationAccessPointFull.pEnd(pEnd));
        }
        if (contract != null) {
            spec = spec.and(SpecificationAccessPointFull.contract(contract));
        }
        if (cStart != null) {
            spec = spec.and(SpecificationAccessPointFull.cStart(cStart));
        }
        if (cEnd != null) {
            spec = spec.and(SpecificationAccessPointFull.cEnd(cEnd));
        }
        Page<AccessPointFull> pageDatas = rAccessPoints.findAll(spec, pageConfig);
        PaginatedList<ReportApContractDTO> pList = new PaginatedList<>(pageDatas.getTotalElements(), pageDatas.stream().map(ReportApContractDTO::new).collect(Collectors.toList()));
        log.info("<-GET /api/report/contract/");
        return pList;
    }

    @GetMapping(
            value = "/export/map/tech"
    )
    @Secured({"ROLE_ADMIN"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportMAPTechData(
            @RequestParam(name = "start", required = false) Long start,
            @RequestParam(name = "end", required = false) Long end) throws IOException {

        Instant instantStart = Instant.ofEpochSecond(start);
        Instant instantEnd = Instant.ofEpochSecond(end);
        instantEnd = instantEnd.minus(1, ChronoUnit.DAYS);

        log.info("->GET /api/report/organization/export/map/:: start:{} end:{}", Converter.simpleDate(instantStart), Converter.simpleDate(instantEnd));
        // xx. go for report data from utm5
        // xx.xx. expand algorithm. idea: go for external systems, suck data. next.
        // go for telecom-aps witch we have data and bind each with each other
        List<UTM5ReportTrafficDTO> dataUtm5 = serviceExternalReports.getReportFromUTM5(start, end);
        List<ZabbixReportDTO> dataZabbix = serviceExternalReports.getReportFromZabbix(start, end);
        List<ReportMapDTO> report = serviceExternalReports.blendData(dataUtm5, dataZabbix);
        IntStream.range(0, report.size()).forEach(i -> report.get(i).setPp(i + 1));
//        List<AccessPoint> report = serviceExternalReports.blendData(dataUtm5, dataZabbix);

        // xx. Forming excel file
        ExportToExcelConfiguration<ReportMapDTO> exportToExcelConfiguration = new ExportToExcelConfiguration<>();
        exportToExcelConfiguration.addColumn(0, Integer.class, ReportMapDTO::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, Integer.class, ReportMapDTO::getUcn, "№ ТЗ");
        exportToExcelConfiguration.addColumn(2, ReportMapDTO::getParent, "Район / гор. округ");
        exportToExcelConfiguration.addColumn(3, ReportMapDTO::getLocation, "Населенный пункт");
        exportToExcelConfiguration.addColumn(4, ReportMapDTO::getAddress, "Адрес");
        exportToExcelConfiguration.addColumn(5, ReportMapDTO::getContractor, "Источник");
        exportToExcelConfiguration.addColumn(6, ReportMapDTO::getOrganization, "Учреждение");

        exportToExcelConfiguration.addColumn(7, ReportMapDTO::getInternetAccessType, "Технология подключения");
        exportToExcelConfiguration.addColumn(8, ReportMapDTO::getZabbixDeviceIp, "IP");
        exportToExcelConfiguration.addColumn(9, ReportMapDTO::getNA, "mask");
        exportToExcelConfiguration.addColumn(10, ReportMapDTO::getNA, "ID");
        exportToExcelConfiguration.addColumn(11, ReportMapDTO::getZabbixDeviceName, "Точка подключени");
        exportToExcelConfiguration.addColumn(12, ReportMapDTO::getNA, "Vlan");
        exportToExcelConfiguration.addColumn(13, ReportMapDTO::getNA, "Признак");
        exportToExcelConfiguration.addColumn(14, ReportMapDTO::getNetworks, "Подсеть клиента");

        ExcelExporter<ReportMapDTO> excelExporter = new ExcelExporter<>(exportToExcelConfiguration);

        // xx. response back an a file
        ByteArrayResource resource = new ByteArrayResource(excelExporter.exportToByteArray(report));
        log.info("<-GET /api/report/organization/export/map/:: start:{} end:{}", Converter.simpleDate(instantStart), Converter.simpleDate(instantEnd));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+ URLEncoder.encode("Отчет_мониторинга_технологии_за_"+ Converter.simpleDate(instantStart) + "-" + Converter.simpleDate(instantEnd), StandardCharsets.UTF_8.toString()) + ".xlsx\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
    @GetMapping(
            value = "/export/map/availability"
    )
    @Secured({"ROLE_ADMIN"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportMAPAvailability(
            @RequestParam(name = "start", required = false) Long start,
            @RequestParam(name = "end", required = false) Long end) throws IOException {

        Instant instantStart = Instant.ofEpochSecond(start);
        Instant instantEnd = Instant.ofEpochSecond(end);
        instantEnd = instantEnd.minus(1, ChronoUnit.DAYS);

        log.info("->GET /api/report/organization/export/map/:: start:{} end:{}", Converter.simpleDate(instantStart), Converter.simpleDate(instantEnd));
        // xx. go for report data from utm5
        // xx.xx. expand algorithm. idea: go for external systems, suck data. next.
        // go for telecom-aps witch we have data and bind each with each other
        List<UTM5ReportTrafficDTO> dataUtm5 = serviceExternalReports.getReportFromUTM5(start, end);
        List<ZabbixReportDTO> dataZabbix = serviceExternalReports.getReportFromZabbix(start, end);
        List<ReportMapDTO> report = serviceExternalReports.blendData(dataUtm5, dataZabbix);
        IntStream.range(0, report.size()).forEach(i -> report.get(i).setPp(i + 1));

        // xx. Forming excel file
        ExportToExcelConfiguration<ReportMapDTO> exportToExcelConfiguration = new ExportToExcelConfiguration<>();
        exportToExcelConfiguration.addColumn(0, Integer.class, ReportMapDTO::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, Integer.class, ReportMapDTO::getUcn, "№ ТЗ");
        exportToExcelConfiguration.addColumn(2, ReportMapDTO::getParent, "Район / гор. округ");
        exportToExcelConfiguration.addColumn(3, ReportMapDTO::getLocation, "Населенный пункт");
        exportToExcelConfiguration.addColumn(4, ReportMapDTO::getAddress, "Адрес");
        exportToExcelConfiguration.addColumn(5, ReportMapDTO::getContractor, "Источник");
        exportToExcelConfiguration.addColumn(6, ReportMapDTO::getOrganization, "Учреждение");
        exportToExcelConfiguration.addColumn(7, ReportMapDTO::getSla, "Доступность УС, %");

        exportToExcelConfiguration.addColumn(8, ReportMapDTO::getSla, "ПД");
        exportToExcelConfiguration.addColumn(9, ReportMapDTO::getSla, "Инт");
        exportToExcelConfiguration.addColumn(10, ReportMapDTO::getSla, "ТС");
        exportToExcelConfiguration.addColumn(11, ReportMapDTO::getSla, "ПГИ");
        exportToExcelConfiguration.addColumn(12, ReportMapDTO::getSla, "ВКС");
        exportToExcelConfiguration.addColumn(13, ReportMapDTO::getSla, "ВС");

        ExcelExporter<ReportMapDTO> excelExporter = new ExcelExporter<>(exportToExcelConfiguration);

        // xx. response back an a file
        ByteArrayResource resource = new ByteArrayResource(excelExporter.exportToByteArray(report));
        log.info("<-GET /api/report/organization/export/map/:: start:{} end:{}", Converter.simpleDate(instantStart), Converter.simpleDate(instantEnd));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+ URLEncoder.encode("Отчет_мониторинга_доступность_за_"+ Converter.simpleDate(instantStart) + "_" + Converter.simpleDate(instantEnd), StandardCharsets.UTF_8.toString()) + ".xlsx\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping(
            value = "/export/map/unavailability"
    )
    @Secured({"ROLE_ADMIN"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportMAPUnavailability(
            @RequestParam(name = "start", required = false) Long start,
            @RequestParam(name = "end", required = false) Long end) throws IOException {

        Instant instantStart = Instant.ofEpochSecond(start);
        Instant instantEnd = Instant.ofEpochSecond(end);
        instantEnd = instantEnd.minus(1, ChronoUnit.DAYS);

        log.info("->GET /api/report/organization/export/map/:: start:{} end:{}", Converter.simpleDate(instantStart), Converter.simpleDate(instantEnd));
        // xx. go for report data from utm5
        // xx.xx. expand algorithm. idea: go for external systems, suck data. next.
        // go for telecom-aps witch we have data and bind each with each other
        List<UTM5ReportTrafficDTO> dataUtm5 = serviceExternalReports.getReportFromUTM5(start, end);
        List<ZabbixReportDTO> dataZabbix = serviceExternalReports.getReportFromZabbix(start, end);
        List<ReportMapDTO> report = serviceExternalReports.blendData(dataUtm5, dataZabbix);
        IntStream.range(0, report.size()).forEach(i -> report.get(i).setPp(i + 1));
//        List<AccessPoint> report = serviceExternalReports.blendData(dataUtm5, dataZabbix);

        // xx. Forming excel file
        ExportToExcelConfiguration<ReportMapDTO> exportToExcelConfiguration = new ExportToExcelConfiguration<>();
        exportToExcelConfiguration.addColumn(0, Integer.class, ReportMapDTO::getPp, "№ п/п");
        exportToExcelConfiguration.addColumn(1, Integer.class, ReportMapDTO::getUcn, "№ ТЗ");
        exportToExcelConfiguration.addColumn(2, ReportMapDTO::getParent, "Район / гор. округ");
        exportToExcelConfiguration.addColumn(3, ReportMapDTO::getLocation, "Населенный пункт");
        exportToExcelConfiguration.addColumn(4, ReportMapDTO::getAddress, "Адрес");
        exportToExcelConfiguration.addColumn(5, ReportMapDTO::getContractor, "Источник");
        exportToExcelConfiguration.addColumn(6, ReportMapDTO::getOrganization, "Учреждение");
        exportToExcelConfiguration.addColumn(7, ReportMapDTO::getProblemTime, "Время недоступности сервиса ПД, мин.");
        exportToExcelConfiguration.addColumn(8, ReportMapDTO::getConsumption, "Количество потребленного трафика сети Интернет, МБ");

        ExcelExporter<ReportMapDTO> excelExporter = new ExcelExporter<>(exportToExcelConfiguration);

        // xx. response back an a file
        ByteArrayResource resource = new ByteArrayResource(excelExporter.exportToByteArray(report));
        log.info("<-GET /api/report/organization/export/map/:: start:{} end:{}", Converter.simpleDate(instantStart), Converter.simpleDate(instantEnd));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+ URLEncoder.encode("Отчет_мониторинга_недоступность_за_"+ Converter.simpleDate(instantStart) + "_" + Converter.simpleDate(instantEnd), StandardCharsets.UTF_8.toString()) + ".xlsx\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping(value = "/ap-all/export/")
//    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportReportAll(
            @RequestParam(name = "location", required = false) Location location,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp,
            @RequestParam(name = "inet", required = false) TypeInternetAccess inettype,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "contractor", required = false) String contractor,
            @RequestParam(name = "ap", required = false) List<TypeAccessPoint> ap,
            @RequestParam(name = "sort", required = false) String sort
    ) throws IOException {

        Sort sortData = HelperReport.getSortRule(sort);
        Specification<AccessPointFull> spec = Specification.where(null);

/*
        Specification<AccessPointFull> spec = this.createSpecificationForSort(sort);
*/

        if (location != null) {
            spec = spec != null ? spec.and(SpecificationAccessPointFull.inLocation(location)) : null;
        }
        if (type != null) {
            spec = spec.and(SpecificationAccessPointFull.withType(type));
        }
        if (smo != null) {
            spec = spec.and(SpecificationAccessPointFull.withSmo(smo));
        }
        if (gdp != null) {
            spec = spec.and(SpecificationAccessPointFull.withGovProgram(gdp));
        }
        if (inettype != null) {
            spec = spec.and(SpecificationAccessPointFull.withInetType(inettype));
        }
        if (parents != null) {
            spec = spec.and(SpecificationAccessPointFull.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(SpecificationAccessPointFull.withOrgname(organization));
        }
        if (contractor != null) {
            spec = spec.and(SpecificationAccessPointFull.withOperator(contractor));
        }
        if (ap != null) {
            spec = spec.and(SpecificationAccessPointFull.type(ap));
        }
        log.info("->GET /api/report/organization/ap-all/export");

        // xx. Forming excel file
        List<AccessPointFull> temp;
        if (sortData == null) temp = rAccessPoints.findAll(spec);
        else temp = rAccessPoints.findAll(spec, sortData);

        List<ExelReportAccessPointFullDTO> rezult = temp
                .stream()
                .map(ExelReportAccessPointFullDTO::new)
                .collect(Collectors.toList());

        IntStream.range(0, rezult.size()).forEach(i -> rezult.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFormat().exportToByteArray(rezult));

        log.info("<-GET /api/report/organization/ap-all/export");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, new ReportName(ExcelExportTypes.ORGANIZATION).toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping(value = "/ap-contract/export/")
//    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportReportContract(
            @RequestParam(name = "location", required = false) Location location,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp,
            @RequestParam(name = "inet", required = false) TypeInternetAccess inettype,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "contractor", required = false) String contractor,
            @RequestParam(name = "population-start", required = false) Integer pStart,
            @RequestParam(name = "population-end", required = false) Integer pEnd,
            @RequestParam(name = "contract", required = false) String contract,
            @RequestParam(name = "contract-start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cStart,
            @RequestParam(name = "contract-end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cEnd,
            @RequestParam(name = "sort", required = false) String sort
    ) throws IOException {

        log.info("->GET /api/report/organization/ap-all/export [location={}, type={}, smo={}, gdp={}, inet={}, parents=xx, orgname={}, operator={} ]",
                location == null ? "" : location.getId(), type, smo, gdp, inettype, organization, contractor);
        //HINT: https://github.com/vijjayy81/spring-boot-jpa-rest-demo-filter-paging-sorting

        Sort sortData = HelperReport.getSortRule(sort);
        Specification<AccessPointFull> spec = Specification.where(SpecificationAccessPointFull.apcontract());

        if (location != null) {
            spec = spec.and(SpecificationAccessPointFull.inLocation(location));
        }
        if (type != null) {
            spec = spec.and(SpecificationAccessPointFull.withType(type));
        }
        if (smo != null) {
            spec = spec.and(SpecificationAccessPointFull.withSmo(smo));
        }
        if (gdp != null) {
            spec = spec.and(SpecificationAccessPointFull.withGovProgram(gdp));
        }
        if (inettype != null) {
            spec = spec.and(SpecificationAccessPointFull.withInetType(inettype));
        }
        if (parents != null) {
            spec = spec.and(SpecificationAccessPointFull.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(SpecificationAccessPointFull.withOrgname(organization));
        }
        if (contractor != null) {
            spec = spec.and(SpecificationAccessPointFull.withOperator(contractor));
        }
        if (pStart != null) {
            spec = spec.and(SpecificationAccessPointFull.pStart(pStart));
        }
        if (pEnd != null) {
            spec = spec.and(SpecificationAccessPointFull.pEnd(pEnd));
        }
        if (contract != null) {
            spec = spec.and(SpecificationAccessPointFull.contract(contract));
        }
        if (cStart != null) {
            spec = spec.and(SpecificationAccessPointFull.cStart(cStart));
        }
        if (cEnd != null) {
            spec = spec.and(SpecificationAccessPointFull.cEnd(cEnd));
        }

        List<AccessPointFull> dbData;
        if (sortData == null) dbData = rAccessPoints.findAll(spec);
        else dbData = rAccessPoints.findAll(spec, sortData);

        List<ExelReportAccessPointFullDTO> rezult = dbData
                .stream()
                .map(ExelReportAccessPointFullDTO::new)
                .collect(Collectors.toList());

        IntStream.range(0, rezult.size()).forEach(i -> rezult.get(i).setPp(i + 1));
        ByteArrayResource resource = new ByteArrayResource(generateExelFormat().exportToByteArray(rezult));

        log.info("<-GET /api/report/organization/ap-all/export");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, new ReportName(ExcelExportTypes.CONTRACT).toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }


}
