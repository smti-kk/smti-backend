package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.*;
import ru.cifrak.telecomit.backend.api.dto.response.ExternalSystemCreateStatusDTO;
import ru.cifrak.telecomit.backend.api.util.Reports.HelperReport;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.exceptions.NotAllowedException;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.repository.specs.OrganizationSpec;
import ru.cifrak.telecomit.backend.service.ServiceAccessPoint;
import ru.cifrak.telecomit.backend.service.ServiceOrganization;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/organization")

public class ApiOrganization {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;
    private final RepositoryLocation rLocation;
    private final RepositorySmoType rTypeSmo;
    private final RepositoryOrganizationType rTypeOrganization;
    private final ServiceAccessPoint accesspoints;
    private final ServiceOrganization sOrganization;


    public ApiOrganization(RepositoryOrganization repository, RepositoryAccessPoints rAccessPoints, RepositoryLocation rLocation, RepositorySmoType rTypeSmo, RepositoryOrganizationType rTypeOrganization, ServiceAccessPoint accesspoints, ServiceOrganization sOrganization) {
        this.rOrganization = repository;
        this.rAccessPoints = rAccessPoints;
        this.rLocation = rLocation;
        this.rTypeSmo = rTypeSmo;
        this.rTypeOrganization = rTypeOrganization;
        this.accesspoints = accesspoints;
        this.sOrganization = sOrganization;
    }

    @GetMapping("/report/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public PaginatedList<ReportOrganizationDTO> reportAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "parents", required = false) List<Location> parents,
            @RequestParam(name = "type", required = false) TypeOrganization type,
            @RequestParam(name = "smo", required = false) TypeSmo smo,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "population-start", required = false) Integer pStart,
            @RequestParam(name = "population-end", required = false) Integer pEnd,
            @RequestParam(name = "organization", required = false) String organization,
            @RequestParam(name = "location", required = false) Location... locations
    ) {
        String locationStr = locations != null
                ? Arrays.stream(locations).map(l -> String.valueOf(l.getId())).collect(Collectors.joining(","))
                : "";
        log.info("->GET /api/organization/report/[page={}, size={}, location={}, orgname={}, type={}, smo={}, sort={}]",
                page, size, locationStr, organization, type, smo, sort);
        //HINT: https://github.com/vijjayy81/spring-boot-jpa-rest-demo-filter-paging-sorting
        Set<String> sortingFileds = new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sort, ""), ",")));

        List<Sort.Order> sortingOrders = sortingFileds.stream().map(HelperReport::getOrder)
                .collect(Collectors.toList());

        Sort sortData = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);
        Pageable pageConfig;
        if (sortData != null) {
            pageConfig = PageRequest.of(page - 1, size, sortData);
        } else {
            pageConfig = PageRequest.of(page - 1, size);
        }
        Specification<ru.cifrak.telecomit.backend.entities.Organization> spec = Specification.where(null);
        if (locations != null && locations.length > 0) {
            boolean locationsNotNull = true;
            for (Location loc : locations) {
                if (loc == null) {
                    locationsNotNull = false;
                    break;
                }
            }
            if (locationsNotNull) {
                spec = spec != null ? spec.and(OrganizationSpec.inLocation(locations)) : null;
            }
        }
        if (type != null) {
            spec = spec.and(OrganizationSpec.withType(type));
        }
        if (smo != null) {
            spec = spec.and(OrganizationSpec.withSmo(smo));
        }
//        if (gdp != null) {
//            spec = spec.and(SpecificationAccessPointFull.withGovProgram(gdp));
//        }
//        if (inettype != null) {
//            spec = spec.and(SpecificationAccessPointFull.withInetType(inettype));
//        }
        if (parents != null) {
            spec = spec.and(OrganizationSpec.inParent(parents));
        }
        if (organization != null) {
            spec = spec.and(OrganizationSpec.withOrgname(organization));
        }
//        if (contractor != null) {
//            spec = spec.and(SpecificationAccessPointFull.withOperator(contractor));
//        }
        if (pStart != null) {
            spec = spec.and(OrganizationSpec.pStart(pStart));
        }
        if (pEnd != null) {
            spec = spec.and(OrganizationSpec.pEnd(pEnd));
        }
//        if (ap != null) {
//            spec = spec.and(SpecificationAccessPointFull.type(ap));
//        }
        Page<Organization> pageDatas = rOrganization.findAll(spec, pageConfig);
        PaginatedList<ReportOrganizationDTO> pList = new PaginatedList<>(pageDatas.getTotalElements(), pageDatas.stream().map(ReportOrganizationDTO::new).collect(Collectors.toList()));
        log.info("<-GET /api/organization/report/");
        return pList;
    }

    @DeleteMapping("/{locationId}")
    @Secured({"ROLE_ADMIN"})
    public void remove(@PathVariable Integer locationId) {
        log.info("zzzz"+String.valueOf(locationId));
        rOrganization.deleteById(locationId);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public List<Organization> list() {
        return rOrganization.findAll();
    }

    @GetMapping(params = "location")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public Page<OrganizationWithAccessPointsDTO> listByLocationId(
            @RequestParam("location") Integer locationId,
            Pageable pageable
    ) {
        Page<Organization> result = rOrganization.findAllByLocationId(locationId, pageable);
        List<OrganizationWithAccessPointsDTO> responseDTOs = result.getContent().stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(responseDTOs, pageable, result.getTotalElements());
    }

    @GetMapping(params = {"location", "accessPoint"})
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public Page<OrganizationWithAccessPointsDTO> listByLocationIdWithAccessPoint(
            @RequestParam("location") Integer locationId,
            @RequestParam("accessPoint") Integer accessPointId,
            Pageable pageable
    ) {
        Page<Organization> result = rOrganization.findAllByLocationIdAndWithoutAccessPoint(
                locationId,
                accessPointId,
                pageable
        );
        OrganizationWithAccessPointsDTO organization = new OrganizationWithAccessPointsDTO(
                rOrganization.findByLocationIdAndWithAccessPoint(
                        locationId,
                        accessPointId
                )
        );
        List<OrganizationWithAccessPointsDTO> responseDTOs = result.getContent().stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList());
        Collections.reverse(responseDTOs);
        responseDTOs.add(organization);
        Collections.reverse(responseDTOs);
        return new PageImpl<>(responseDTOs, pageable, result.getTotalElements());
    }

    @GetMapping(value = "/without-ap", params = {"location", "accessPoint"})
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public Page<OrganizationWithAccessPointsDTO> listByLocationIdWithoutAccessPoint(
            @RequestParam("location") Integer locationId,
            @RequestParam("accessPoint") Integer accessPointId,
            Pageable pageable
    ) {
        Page<Organization> result = rOrganization.findAllByLocationIdAndWithoutAccessPoint(
                locationId,
                accessPointId,
                pageable
        );
        List<OrganizationWithAccessPointsDTO> responseDTOs = result.getContent().stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(responseDTOs, pageable, result.getTotalElements());
    }

    @GetMapping("/{locationId}/count")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public Integer countByLocationId(@PathVariable Integer locationId) {
        return rOrganization.countAllByLocationId(locationId);
    }

    @GetMapping("/{id}/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public OrganizationDTO item(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/", id);
        return rOrganization.findById(id).map(OrganizationDTO::new).orElse(null);
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public ResponseEntity<OrganizationShortDTO> createItem(@RequestBody OrganizationShortDTO value,
                                                           @AuthenticationPrincipal User user) {
        log.info("->POST /api/organization/ ");
        Organization item = new Organization();
        item.setAddress(value.getAddress());
        item.setFias(value.getFias());
        item.setName(value.getName());
        item.setInn(value.getInn());
        item.setKpp(value.getKpp());
        item.setAcronym(value.getAcronym());
        item.setLocation(rLocation.getOne(value.getLocation()));
        //TODO: make this work later, when we have some real data...
        List<Organization> parents = rOrganization.findByUserOrganization(user.getId());
        if (parents.size() > 0) {
            item.setParent(parents.get(0));
        }
//        item.setParent(value.getParent());
//        item.setChildren(value.getChildren());
        if (value.getType() != null) {
            item.setType(rTypeOrganization.getOne(value.getType()));
        }
        if (value.getSmo() != null) {
            item.setSmo(rTypeSmo.getOne(value.getSmo()));
        }
        Organization saved = rOrganization.save(item);
        log.info("<-POST /api/organization/{}", item.getId());
        return ResponseEntity.ok(new OrganizationShortDTO(saved));
    }


    @Transactional
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable(name = "id") Organization item, @RequestBody OrganizationShortDTO value) {
        log.info("->PUT /api/organization/{}", item.getId());
        item.setAddress(value.getAddress());
        item.setFias(value.getFias());
        item.setName(value.getName());
        item.setInn(value.getInn());
        item.setKpp(value.getKpp());
        item.setAcronym(value.getAcronym());
        item.setLocation(rLocation.getOne(value.getLocation()));
        //TODO: make this work later, when we have some real data...
//        item.setParent(value.getParent());
//        item.setChildren(value.getChildren());
        if (value.getType() == null) {
            item.setType(null);
        } else {
            item.setType(rTypeOrganization.getOne(value.getType()));
        }
        if (value.getSmo() == null) {
            item.setSmo(null);
        } else {
            item.setSmo(rTypeSmo.getOne(value.getSmo()));
        }
        Organization saved = rOrganization.save(item);
        log.info("<-PUT /api/organization/{}", item.getId());
        return ResponseEntity.ok(new OrganizationDTO(saved));
    }

    @GetMapping("/{id}/ap/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public List<AccessPointDetailInOrganizationDTO> apsByOrganization(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/ap", id);
        return rAccessPoints.getAllByOrganizationIdAndDeletedIsFalse(id).stream().map(AccessPointDetailInOrganizationDTO::new).collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    @PostMapping(value = "/{id}/ap/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAP(
            @PathVariable(name = "id") final Organization organization,
            @RequestBody final AccessPointNewDTO dto
    ) {
        log.info("->POST /api/organization/{}/ap", organization.getId());
        AccessPointDetailInOrganizationDTO bNew;
        try {
            bNew = accesspoints.giveNewCreatedAccessPoint(organization, dto);
            log.info("<-POST /api/organization/{}/ap/{}", organization.getId(), bNew.getId());
            return ResponseEntity.ok(bNew);
        } catch (Exception e) {
            log.error("<-POST /api/organization/{}/ap/ :: {}", organization.getId(), e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .header("Content-Type", "application/json")
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    @PutMapping(value = "/{id}/ap/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateAP(
            @PathVariable(name = "id") final Organization organization,
            @RequestBody final AccessPointNewDTO dto
    ) {
        log.info("->PUT /api/organization/{}/ap", organization.getId());
        AccessPointDetailInOrganizationDTO bNew;
        try {
            bNew = accesspoints.giveNewCreatedAccessPoint(organization, dto);
            log.info("<-PUT /api/organization/{}/ap/{}", organization.getId(), bNew.getId());
            return ResponseEntity.ok(bNew);
        } catch (Exception e) {
            log.error("<-PUT /api/organization/{}/ap/ :: {}", organization.getId(), e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .header("Content-Type", "application/json")
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{id}/ap/{apid}/init-monitoring")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public ExternalSystemCreateStatusDTO initMonitoring(@PathVariable Integer id, @PathVariable Integer apid,
                                                        @RequestBody final MonitoringAccessPointWizardDTO wizard, @AuthenticationPrincipal User user) throws NotAllowedException {
        log.info("[{}]->GET /{}/ap/{}/init-monitoring", user.getUsername(), id, apid);
        try {
             ExternalSystemCreateStatusDTO result = sOrganization.linkAccessPointWithMonitoringSystems(id, apid, wizard);
            log.info("[{}]<-GET /{}/ap/{}/init-monitoring", user.getUsername(), id, apid);
            return result;
        } catch (Exception e) {
            log.warn("[{}]<-GET /{}/ap/{}/init-monitoring :EXCEPTION {}", user.getUsername(), id, apid, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/base/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_OPERATOR", "ROLE_MUNICIPALITY"})
    public List<Organization> base() {
        return rOrganization.findAllMain();
    }
}
