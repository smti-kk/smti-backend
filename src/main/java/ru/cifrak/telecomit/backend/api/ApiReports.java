package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.api.dto.ReportAccessPointFullDTO;
import ru.cifrak.telecomit.backend.api.dto.ReportApContractDTO;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryApContract;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.specs.SpecificationAccessPoint;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j

@RestController
@RequestMapping("/api/report/organization")
public class ApiReports {
    private final RepositoryLocation rLocation;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryApContract rApContract;

    @Autowired
    public ApiReports(RepositoryLocation repository, RepositoryLocation rLocation, RepositoryAccessPoints rAccessPoints, RepositoryApContract rApContract) {
        this.rLocation = rLocation;
        this.rAccessPoints = rAccessPoints;
        this.rApContract = rApContract;
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
            @RequestParam(name = "ap", required = false) List<String> ap,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        log.info("->GET /api/report/organization/[page={}, size={}, location={}, type={}, smo={}, gdp={}, inet={}, parents=xx, orgname={}, operator={} ]",
                page, size, location == null ? "" : location.getId(), type, smo, gdp, inettype, organization, contractor);
        //HINT: https://github.com/vijjayy81/spring-boot-jpa-rest-demo-filter-paging-sorting
        Set<String> sortingFileds = new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sort, ""), ",")));

        List<Sort.Order> sortingOrders = sortingFileds.stream().map(this::getOrder)
                .collect(Collectors.toList());

        Sort sortData = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);
        Pageable pageConfig;
        if (sortData != null) {
            pageConfig = PageRequest.of(page - 1, size, sortData);
        } else {
            pageConfig = PageRequest.of(page - 1, size);
        }
        Specification<AccessPoint> spec = Specification.where(null);
        if (location != null) {
            spec = spec.and(SpecificationAccessPoint.inLocation(location));
        }
        if (type != null) {
            spec = spec.and(SpecificationAccessPoint.withType(type));
        }
        if (smo != null) {
            spec = spec.and(SpecificationAccessPoint.withSmo(smo));
        }
        if (gdp != null) {
            spec = spec.and(SpecificationAccessPoint.withGovProgram(gdp));
        }
        if (inettype != null) {
            spec = spec.and(SpecificationAccessPoint.withInetType(inettype));
        }
        if (parents != null) {
            spec = spec.and(SpecificationAccessPoint.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(SpecificationAccessPoint.withOrgname(organization));
        }
        if (contractor != null) {
            spec = spec.and(SpecificationAccessPoint.withOperator(contractor));
        }
        if (pStart != null) {
            spec = spec.and(SpecificationAccessPoint.pStart(pStart));
        }
        if (pEnd != null) {
            spec = spec.and(SpecificationAccessPoint.pEnd(pEnd));
        }/*
        if (ap != null) {
            spec = spec.and(SpecificationAccessPoint.ap(ap));
        }*/
        Page<AccessPoint> pageDatas = rAccessPoints.findAll(spec, pageConfig);
        PaginatedList<ReportAccessPointFullDTO> pList = new PaginatedList<>(pageDatas.getTotalElements(), pageDatas.stream().map(ReportAccessPointFullDTO::new).collect(Collectors.toList()));
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
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sort, ""), ",")));

        List<Sort.Order> sortingOrders = sortingFileds.stream().map(this::getOrder)
                .collect(Collectors.toList());

        Sort sortData = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);
        Pageable pageConfig;
        if (sortData != null) {
            pageConfig = PageRequest.of(page - 1, size, sortData);
        } else {
            pageConfig = PageRequest.of(page - 1, size);
        }
        Specification<AccessPoint> spec = Specification.where(null);
        if (location != null) {
            spec = spec.and(SpecificationAccessPoint.inLocation(location));
        }
        if (type != null) {
            spec = spec.and(SpecificationAccessPoint.withType(type));
        }
        if (smo != null) {
            spec = spec.and(SpecificationAccessPoint.withSmo(smo));
        }
        if (gdp != null) {
            spec = spec.and(SpecificationAccessPoint.withGovProgram(gdp));
        }
        if (inettype != null) {
            spec = spec.and(SpecificationAccessPoint.withInetType(inettype));
        }
        if (parents != null) {
            spec = spec.and(SpecificationAccessPoint.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(SpecificationAccessPoint.withOrgname(organization));
        }
        if (contractor != null) {
            spec = spec.and(SpecificationAccessPoint.withOperator(contractor));
        }
        if (pStart != null) {
            spec = spec.and(SpecificationAccessPoint.pStart(pStart));
        }
        if (pEnd != null) {
            spec = spec.and(SpecificationAccessPoint.pEnd(pEnd));
        }
        if (contract != null) {
            spec = spec.and(SpecificationAccessPoint.contract(contract));
        }
        if (cStart != null) {
            spec = spec.and(SpecificationAccessPoint.cStart(cStart));
        }
        if (cEnd != null) {
            spec = spec.and(SpecificationAccessPoint.cEnd(cEnd));
        }
        Page<ApContract> pageDatas = rApContract.findAll(spec, pageConfig);
        PaginatedList<ReportApContractDTO> pList = new PaginatedList<>(pageDatas.getTotalElements(), pageDatas.stream().map(ReportApContractDTO::new).collect(Collectors.toList()));
        log.info("<-GET /api/report/contract/");
        return pList;
    }

    private Sort.Order getOrder(String value) {

        if (StringUtils.startsWith(value, "-")) {
            return new Sort.Order(Sort.Direction.DESC, StringUtils.substringAfter(value, "-"));
        } else if (StringUtils.startsWith(value, "+")) {
            return new Sort.Order(Sort.Direction.ASC, StringUtils.substringAfter(value, "+"));
        } else {
            // Sometimes '+' from query param can be replaced as ' '
            return new Sort.Order(Sort.Direction.ASC, StringUtils.trim(value));
        }

    }

}
