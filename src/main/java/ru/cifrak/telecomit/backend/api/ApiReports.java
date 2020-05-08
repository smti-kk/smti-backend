package ru.cifrak.telecomit.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationReportOrganizationDTO;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class ApiReports {
    private RepositoryLocation repository;

    @Autowired
    public ApiReports(RepositoryLocation repository) {
        this.repository = repository;
    }

    //TODO: TELECOMIT-155 !!!WIP!!! proper select
    @GetMapping("/report-organization/")
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
    }

}
