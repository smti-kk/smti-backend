package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
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
import ru.cifrak.telecomit.backend.api.dto.AccessPointDTOReportOgranization;
import ru.cifrak.telecomit.backend.api.dto.OrganizationWithAccessPointsDTO;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;
import ru.cifrak.telecomit.backend.repository.specs.SpecificationAccessPoint;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//    public PaginatedList<Map<Organization, List<AccessPoint>>> items(
    public PaginatedList<AccessPointDTOReportOgranization> items(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "location", required = false) Location location,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "gdp", required = false) GovernmentDevelopmentProgram gdp
    ) {
        log.info("->GET /api/report/organization/");
        Pageable pageConfig = PageRequest.of(page-1, size, Sort.by("organization").ascending());
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
        Page<AccessPoint> pageDatas = rAccessPoints.findAll(spec, pageConfig);
//        Map<Organization, List<AccessPoint>> mapData = new HashMap<>();
//        mapData = pageDatas.stream()
//                .collect(Collectors.groupingBy(AccessPoint::getOrganization));
//        List<OrganizationWithAccessPointsDTO> rezults
        ;
        List <AccessPointDTOReportOgranization> rzt =pageDatas.getContent().stream().map(AccessPointDTOReportOgranization::new).collect(Collectors.toList());
        log.info("collected map");
//        PaginatedList<Map<Organization, List<AccessPoint>>> pList = new PaginatedList<Map<Organization, List<AccessPoint>>>(pageDatas.getTotalElements(), mapData);
        PaginatedList pList = new PaginatedList<AccessPointDTOReportOgranization>(
                pageDatas.getTotalElements(),
                rzt
        );
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

}
