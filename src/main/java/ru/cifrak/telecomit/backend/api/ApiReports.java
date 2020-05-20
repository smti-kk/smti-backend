package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.api.dto.ReportGroupOrganizaationDTO;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.specs.SpecificationAccessPoint;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j

@RestController
@RequestMapping("/api/report/organization")
public class ApiReports {
    private final RepositoryLocation rLocation;
    private final RepositoryAccessPoints rAccessPoints;

    @Autowired
    public ApiReports(RepositoryLocation repository, RepositoryLocation rLocation, RepositoryAccessPoints rAccessPoints) {
        this.rLocation = rLocation;
        this.rAccessPoints = rAccessPoints;
    }

    @GetMapping("/ap-all/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public PaginatedList<ReportGroupOrganizaationDTO> items(
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
        Page<AccessPoint> pageDatas = rAccessPoints.findAll(spec, pageConfig);
        Map<Organization, List<AccessPoint>> mapData = pageDatas.stream().collect(Collectors.groupingBy(AccessPoint::getOrganization));
        List<ReportGroupOrganizaationDTO> rezultListofOrganizations = mapData.entrySet().stream()
                .map(entry -> new ReportGroupOrganizaationDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        PaginatedList<ReportGroupOrganizaationDTO> pList = new PaginatedList<>(pageDatas.getTotalElements(), rezultListofOrganizations);
        log.info("<-GET /api/report/organization/");
        return pList;
    }

/*

    //TODO: TELECOMIT-155 !!!WIP!!! proper select
    @GetMapping("/ap-all/")
    public PaginatedList<LocationReportOrganizationDTO> organizations(@RequestParam @Min(1) int page, @RequestParam int page_size) {
        Pageable pageable = PageRequest.of(page - 1, page_size);
        long count = repository.count();
        final List<LocationReportOrganizationDTO> list = repository.findAllReportOrganization(pageable).stream()
                .map(LocationReportOrganizationDTO::new)
                .collect(Collectors.toList());
        return new PaginatedList<LocationReportOrganizationDTO>(count, "", "", list);
    }

    //TODO: TELECOMIT-155 !!!WIP!!! proper select
    @GetMapping("/report-organization-contracts/")
    public PaginatedList<LocationReportOrganizationDTO> contracts(@RequestParam @Min(1) int page, @RequestParam int page_size) {
        Pageable pageable = PageRequest.of(page - 1, page_size);
        long count = repository.count();
        final List<LocationReportOrganizationDTO> list = repository.findAllReportOrganization(pageable).stream()
                .map(LocationReportOrganizationDTO::new)
                .collect(Collectors.toList());
        return new PaginatedList<LocationReportOrganizationDTO>(count, "", "", list);
    }*/

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
