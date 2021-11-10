package ru.cifrak.telecomit.backend.api.util.reports;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import ru.cifrak.telecomit.backend.repository.specs.AccessPointFullSpecification;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.cifrak.telecomit.backend.api.util.reports.HelperReport.generateExelFormat;

@Slf4j
@RestController
@RequestMapping("/api/report/organization")
public class ApiReports {
    private final RepositoryAccessPointsFull rAccessPoints;
    private final ServiceExternalReports serviceExternalReports;


    @Autowired
    public ApiReports(RepositoryAccessPointsFull rAccessPoints, ServiceExternalReports serviceExternalReports) {
        this.rAccessPoints = rAccessPoints;
        this.serviceExternalReports = serviceExternalReports;
    }

    @GetMapping(value = "/ap-all/export/")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportReportAll(
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp,
            @RequestParam(name = "inet", required = false) TypeInternetAccess inettype,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "contractor", required = false) String contractor,
            @RequestParam(name = "population-start", required = false) Integer pStart,
            @RequestParam(name = "population-end", required = false) Integer pEnd,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "ap", required = false) List<TypeAccessPoint> ap,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @RequestParam(name = "location", required = false) Location... locations) throws IOException {
        log.info("--> GET /api/report/organization/ap-all/export/");
        ByteArrayResource resource = getResource(type, smo, gdp, inettype, parents, organization, contractor,
                pStart, pEnd, address, ap, sort, logicalCondition, locations);
        log.info("<-- GET /api/report/organization/ap-all/export/");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, new ReportName(ExcelExportTypes.ORGANIZATION).toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @NotNull
    private ByteArrayResource getResource(TypeOrganization type,
                                          TypeSmo smo,
                                          GovernmentDevelopmentProgram gdp,
                                          TypeInternetAccess inettype,
                                          List<Location> parents,
                                          String organization,
                                          String contractor,
                                          Integer pStart,
                                          Integer pEnd,
                                          String contract,
                                          LocalDate cStart,
                                          LocalDate cEnd,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) throws IOException {
        List<ExelReportAccessPointFullDTO> result = getData(
                type, smo, gdp, inettype, parents, organization,
                contractor, pStart, pEnd, contract, cStart, cEnd, sort, logicalCondition, locations)
                .stream()
                .map(ExelReportAccessPointFullDTO::new)
                .collect(Collectors.toList());
        IntStream.range(0, result.size()).forEach(i -> result.get(i).setPp(i + 1));
        return new ByteArrayResource(generateExelFormat().exportToByteArray(result));
    }

    @NotNull
    private ByteArrayResource getResource(TypeOrganization type,
                                          TypeSmo smo,
                                          GovernmentDevelopmentProgram gdp,
                                          TypeInternetAccess inettype,
                                          List<Location> parents,
                                          String organization,
                                          String contractor,
                                          Integer pStart,
                                          Integer pEnd,
                                          String address,
                                          List<TypeAccessPoint> ap,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) throws IOException {
        List<ExelReportAccessPointFullDTO> result = getData(
                type, smo, gdp, inettype, parents, organization,
                contractor, pStart, pEnd, address, ap, sort, logicalCondition, locations)
                .stream()
                .map(ExelReportAccessPointFullDTO::new)
                .collect(Collectors.toList());
        IntStream.range(0, result.size()).forEach(i -> result.get(i).setPp(i + 1));
        return new ByteArrayResource(generateExelFormat().exportToByteArray(result));
    }

    @GetMapping("/ap-all/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public PaginatedList<ReportAccessPointFullDTO> reportAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp,
            @RequestParam(name = "inet", required = false) TypeInternetAccess inettype,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "contractor", required = false) String contractor,
            @RequestParam(name = "population-start", required = false) Integer pStart,
            @RequestParam(name = "population-end", required = false) Integer pEnd,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "ap", required = false) List<TypeAccessPoint> ap,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @RequestParam(name = "location", required = false) Location... locations) {
        log.info("--> GET /api/report/organization/ap-all/");
        PaginatedList<ReportAccessPointFullDTO> pList = getPListReportAccessPointFullDTO(getData(
                page, size, type, smo, gdp, inettype, parents, organization,
                contractor, pStart, pEnd, address, ap, sort, logicalCondition, locations));
        log.info("<-- GET /api/report/organization/ap-all/");
        return pList;
    }

    private List<AccessPointFull> getData(TypeOrganization type,
                                          TypeSmo smo,
                                          GovernmentDevelopmentProgram gdp,
                                          TypeInternetAccess inettype,
                                          List<Location> parents,
                                          String organization,
                                          String contractor,
                                          Integer pStart,
                                          Integer pEnd,
                                          String address,
                                          List<TypeAccessPoint> ap,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) {
        return rAccessPoints.findAll(getSpec(type, smo, gdp, inettype, parents, organization, contractor,
                        pStart, pEnd, address, ap, logicalCondition, locations),
                Sort.by(getSortingOrders(sort)));
    }

    private List<AccessPointFull> getData(TypeOrganization type,
                                          TypeSmo smo,
                                          GovernmentDevelopmentProgram gdp,
                                          TypeInternetAccess inettype,
                                          List<Location> parents,
                                          String organization,
                                          String contractor,
                                          Integer pStart,
                                          Integer pEnd,
                                          String contract,
                                          LocalDate cStart,
                                          LocalDate cEnd,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) {
        return rAccessPoints.findAll(getSpec(type, smo, gdp, inettype, parents, organization, contractor,
                        pStart, pEnd, contract, cStart, cEnd, logicalCondition, locations),
                Sort.by(getSortingOrders(sort)));
    }

    private Page<AccessPointFull> getData(int page,
                                          int size,
                                          TypeOrganization type,
                                          TypeSmo smo,
                                          GovernmentDevelopmentProgram gdp,
                                          TypeInternetAccess inettype,
                                          List<Location> parents,
                                          String organization,
                                          String contractor,
                                          Integer pStart,
                                          Integer pEnd,
                                          String contract,
                                          LocalDate cStart,
                                          LocalDate cEnd,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) {
        return rAccessPoints.findAll(getSpec(type, smo, gdp, inettype, parents, organization, contractor,
                        pStart, pEnd, contract, cStart, cEnd, logicalCondition, locations),
                PageRequest.of(page - 1, size, Sort.by(getSortingOrders(sort))));
    }

    private Page<AccessPointFull> getData(int page,
                                          int size,
                                          TypeOrganization type,
                                          TypeSmo smo,
                                          GovernmentDevelopmentProgram gdp,
                                          TypeInternetAccess inettype,
                                          List<Location> parents,
                                          String organization,
                                          String contractor,
                                          Integer pStart,
                                          Integer pEnd,
                                          String address,
                                          List<TypeAccessPoint> ap,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) {
        return rAccessPoints.findAll(getSpec(type, smo, gdp, inettype, parents, organization, contractor,
                        pStart, pEnd, address, ap, logicalCondition, locations),
                PageRequest.of(page - 1, size, Sort.by(getSortingOrders(sort))));
    }

    @NotNull
    private PaginatedList<ReportApContractDTO> getPListReportApContractDTO(Page<AccessPointFull> pData) {
        return new PaginatedList<>(
                pData.getTotalElements(),
                pData.stream()
                        .map(ReportApContractDTO::new)
                        .collect(Collectors.toList())
        );
    }

    @NotNull
    private PaginatedList<ReportAccessPointFullDTO> getPListReportAccessPointFullDTO(Page<AccessPointFull> pData) {
        return new PaginatedList<>(
                pData.getTotalElements(),
                pData.stream()
                        .map(ReportAccessPointFullDTO::new)
                        .collect(Collectors.toList())
        );
    }

    @Nullable
    private Specification<AccessPointFull> getSpec(TypeOrganization type,
                                                   TypeSmo smo,
                                                   GovernmentDevelopmentProgram gdp,
                                                   TypeInternetAccess inettype,
                                                   List<Location> parents,
                                                   String organization,
                                                   String contractor,
                                                   Integer pStart,
                                                   Integer pEnd,
                                                   String contract,
                                                   LocalDate cStart,
                                                   LocalDate cEnd,
                                                   LogicalCondition logicalCondition,
                                                   Location[] locations) {
        List<Specification<AccessPointFull>> specs = getSpecs(type, smo, gdp, inettype, parents, organization,
                contractor, contract, cStart, cEnd, locations);
        Specification<AccessPointFull> mainSpec = logicalCondition == LogicalCondition.OR ?
                getSpecsWithOrCondition(specs)
                : getSpecsWithAndCondition(specs);
        return getSpecsWithAndCondition(getNecessarySpecs(pStart, pEnd)).and(mainSpec);
    }

    @Nullable
    private Specification<AccessPointFull> getSpec(TypeOrganization type,
                                                   TypeSmo smo,
                                                   GovernmentDevelopmentProgram gdp,
                                                   TypeInternetAccess inettype,
                                                   List<Location> parents,
                                                   String organization,
                                                   String contractor,
                                                   Integer pStart,
                                                   Integer pEnd,
                                                   String address,
                                                   List<TypeAccessPoint> ap,
                                                   LogicalCondition logicalCondition,
                                                   Location[] locations) {
        List<Specification<AccessPointFull>> specs = getSpecs(type, smo, gdp, inettype, parents, organization,
                contractor, address, ap, locations);
        Specification<AccessPointFull> mainSpec = logicalCondition == LogicalCondition.OR ?
                getSpecsWithOrCondition(specs)
                : getSpecsWithAndCondition(specs);
        return getSpecsWithAndCondition(getNecessarySpecs(pStart, pEnd)).and(mainSpec);
    }

    private Specification<AccessPointFull> getSpecsWithOrCondition(List<Specification<AccessPointFull>> specs) {
        AtomicReference<Specification<AccessPointFull>> result =
                new AtomicReference<>(AccessPointFullSpecification.FALSE_SPEC);
        specs.forEach(spec -> result.set(
                Objects.requireNonNull(result.get().or(AccessPointFullSpecification.forOrCondition(spec)))));
        return result.get();
    }

    private Specification<AccessPointFull> getSpecsWithAndCondition(List<Specification<AccessPointFull>> specs) {
        AtomicReference<Specification<AccessPointFull>> result =
                new AtomicReference<>(AccessPointFullSpecification.TRUE_SPEC);
        specs.forEach(spec -> result.set(
                Objects.requireNonNull(result.get().and(AccessPointFullSpecification.forAndCondition(spec)))));
        return result.get();
    }

    private List<Specification<AccessPointFull>> getNecessarySpecs(Integer pStart, Integer pEnd) {
        List<Specification<AccessPointFull>> result = new ArrayList<>();
        result.add(getSpecPStart(pStart));
        result.add(getSpecPEnd(pEnd));
        return result;
    }

    private List<Specification<AccessPointFull>> getSpecs(TypeOrganization type,
                                                          TypeSmo smo,
                                                          GovernmentDevelopmentProgram gdp,
                                                          TypeInternetAccess inettype,
                                                          List<Location> parents,
                                                          String organization,
                                                          String contractor,
                                                          String contract,
                                                          LocalDate cStart,
                                                          LocalDate cEnd,
                                                          Location[] locations) {
        List<Specification<AccessPointFull>> result = new ArrayList<>();
        result.add(getSpecLocations(locations));
        result.add(getSpecType(type));
        result.add(getSpecSmo(smo));
        result.add(getSpecGdp(gdp));
        result.add(getSpecInettype(inettype));
        result.add(getSpecParents(parents));
        result.add(getSpecOrganization(organization));
        result.add(getSpecContractor(contractor));
        result.add(getSpecContract(contract));
        result.add(getSpecCStart(cStart));
        result.add(getSpecCEnd(cEnd));
        return result;
    }

    private List<Specification<AccessPointFull>> getSpecs(TypeOrganization type,
                                                          TypeSmo smo,
                                                          GovernmentDevelopmentProgram gdp,
                                                          TypeInternetAccess inettype,
                                                          List<Location> parents,
                                                          String organization,
                                                          String contractor,
                                                          String address,
                                                          List<TypeAccessPoint> ap,
                                                          Location[] locations) {
        List<Specification<AccessPointFull>> result = new ArrayList<>();
        result.add(getSpecLocations(locations));
        result.add(getSpecType(type));
        result.add(getSpecSmo(smo));
        result.add(getSpecGdp(gdp));
        result.add(getSpecInettype(inettype));
        result.add(getSpecParents(parents));
        result.add(getSpecOrganization(organization));
        result.add(getSpecContractor(contractor));
        result.add(getSpecAp(ap));
        result.add(getSpecAddress(address));
        return result;
    }

    private Specification<AccessPointFull> getSpecContract(String contract) {
        return contract != null ?
                AccessPointFullSpecification.contract(contract)
                : null;
    }

    private Specification<AccessPointFull> getSpecCStart(LocalDate cStart) {
        return cStart != null ?
                AccessPointFullSpecification.cStart(cStart)
                : null;
    }

    private Specification<AccessPointFull> getSpecCEnd(LocalDate cEnd) {
        return cEnd != null ?
                AccessPointFullSpecification.cEnd(cEnd)
                : null;
    }

    private Specification<AccessPointFull> getSpecAddress(String address) {
        return address != null ?
                AccessPointFullSpecification.inAddress(address)
                : null;
    }

    private Specification<AccessPointFull> getSpecAp(List<TypeAccessPoint> ap) {
        return ap != null ?
                AccessPointFullSpecification.type(ap)
                : null;
    }

    private Specification<AccessPointFull> getSpecPEnd(Integer pEnd) {
        return pEnd != null ?
                AccessPointFullSpecification.pEnd(pEnd)
                : null;
    }

    private Specification<AccessPointFull> getSpecPStart(Integer pStart) {
        return pStart != null ?
                AccessPointFullSpecification.pStart(pStart)
                : null;
    }

    private Specification<AccessPointFull> getSpecContractor(String contractor) {
        return contractor != null ?
                AccessPointFullSpecification.withOperator(contractor)
                : null;
    }

    private Specification<AccessPointFull> getSpecOrganization(String organization) {
        return organization != null ?
                AccessPointFullSpecification.withOrgname(organization)
                : null;
    }

    private Specification<AccessPointFull> getSpecParents(List<Location> parents) {
        return parents != null ?
                AccessPointFullSpecification.inParent(parents)
                : null;
    }

    private Specification<AccessPointFull> getSpecInettype(TypeInternetAccess inettype) {
        return inettype != null ?
                AccessPointFullSpecification.withInetType(inettype)
                : null;
    }

    private Specification<AccessPointFull> getSpecGdp(GovernmentDevelopmentProgram gdp) {
        return gdp != null ?
                AccessPointFullSpecification.withGovProgram(gdp)
                : null;
    }

    private Specification<AccessPointFull> getSpecSmo(TypeSmo smo) {
        return smo != null ?
                AccessPointFullSpecification.withSmo(smo)
                : null;
    }

    private Specification<AccessPointFull> getSpecType(TypeOrganization type) {
        return type != null ?
                AccessPointFullSpecification.withType(type)
                : null;
    }

    @Nullable
    private Specification<AccessPointFull> getSpecLocations(Location[] locations) {
        return locationsNotNull(locations) ?
                AccessPointFullSpecification.inLocations(locations)
                : null;
    }

    private boolean locationsNotNull(Location[] locations) {
        boolean locationsNotNull;
        if (locations != null) {
            locationsNotNull = true;
            for (Location loc : locations) {
                if (loc == null) {
                    locationsNotNull = false;
                    break;
                }
            }
        } else {
            locationsNotNull = false;
        }
        return locationsNotNull;
    }

    @NotNull
    private List<Sort.Order> getSortingOrders(String sort) {
        return new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sort, ""), ",")))
                .stream()
                .map(HelperReport::getOrder)
                .collect(Collectors.toList());
    }

    @GetMapping("/ap-contract/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public PaginatedList<ReportApContractDTO> reportContracts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
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
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @RequestParam(name = "location", required = false) Location... locations
    ) {
        log.info("--> GET /api/report/organization/ap-contract/");
        PaginatedList<ReportApContractDTO> pList = getPListReportApContractDTO(getData(
                page, size, type, smo, gdp, inettype, parents, organization,
                contractor, pStart, pEnd, contract, cStart, cEnd, sort, logicalCondition, locations));
        log.info("<-- GET /api/report/organization/ap-contract/");
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
        exportToExcelConfiguration.addColumn(9, ReportMapDTO::getMask, "mask");
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
                        "attachment; filename=\"" + URLEncoder.encode("Отчет_мониторинга_технологии_за_" + Converter.simpleDate(instantStart) + "-" + Converter.simpleDate(instantEnd), StandardCharsets.UTF_8.toString()) + ".xlsx\"")
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
                        "attachment; filename=\"" + URLEncoder.encode("Отчет_мониторинга_доступность_за_" + Converter.simpleDate(instantStart) + "_" + Converter.simpleDate(instantEnd), StandardCharsets.UTF_8.toString()) + ".xlsx\"")
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
                        "attachment; filename=\"" + URLEncoder.encode("Отчет_мониторинга_недоступность_за_" + Converter.simpleDate(instantStart) + "_" + Converter.simpleDate(instantEnd), StandardCharsets.UTF_8.toString()) + ".xlsx\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping(value = "/ap-contract/export/")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> exportReportContract(
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
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @RequestParam(name = "location", required = false) Location... locations) throws IOException {
        log.info("--> GET /api/report/organization/ap-all/export/");
        ByteArrayResource resource = getResource(type, smo, gdp, inettype, parents, organization, contractor,
                pStart, pEnd, contract, cStart, cEnd, sort, logicalCondition, locations);
        log.info("<-- GET /api/report/organization/ap-all/export/");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, new ReportName(ExcelExportTypes.CONTRACT).toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
