package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.*;
import ru.cifrak.telecomit.backend.api.dto.response.ExternalSystemCreateStatusDTO;
import ru.cifrak.telecomit.backend.api.util.reports.HelperReport;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.exceptions.NotAllowedException;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.repository.specs.OrganizationSpec;
import ru.cifrak.telecomit.backend.service.ServiceAccessPoint;
import ru.cifrak.telecomit.backend.service.ServiceOrganization;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    private final RepositoryFunCustomer rFunCustomer;
    private final ServiceAccessPoint accesspoints;
    private final ServiceOrganization sOrganization;


    public ApiOrganization(RepositoryOrganization repository, RepositoryAccessPoints rAccessPoints, RepositoryLocation rLocation, RepositorySmoType rTypeSmo, RepositoryOrganizationType rTypeOrganization, RepositoryFunCustomer rFunCustomer, ServiceAccessPoint accesspoints, ServiceOrganization sOrganization) {
        this.rOrganization = repository;
        this.rAccessPoints = rAccessPoints;
        this.rLocation = rLocation;
        this.rTypeSmo = rTypeSmo;
        this.rTypeOrganization = rTypeOrganization;
        this.rFunCustomer = rFunCustomer;
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
            @RequestParam(name = "logicalCondition", required = false) LogicalCondition logicalCondition,
            @RequestParam(name = "location", required = false) Location... locations
    ) {
        log.info("--> GET /api/organization/report/");
        PaginatedList<ReportOrganizationDTO> pList = getPListReportOrganizationDTO(getData(
                page, size, type, smo, parents, organization, pStart, pEnd,
                sort, logicalCondition, locations));
        log.info("<-- GET /api/organization/report/");
        return pList;
    }

    private Page<Organization> getData(int page,
                                          int size,
                                          TypeOrganization type,
                                          TypeSmo smo,
                                          List<Location> parents,
                                          String organization,
                                          Integer pStart,
                                          Integer pEnd,
                                          String sort,
                                          LogicalCondition logicalCondition,
                                          Location[] locations) {
        return rOrganization.findAll(getSpec(type, smo, parents, organization, pStart, pEnd,
                        logicalCondition, locations),
                PageRequest.of(page - 1, size, Sort.by(getSortingOrders(sort))));
    }

    @NotNull
    private List<Sort.Order> getSortingOrders(String sort) {
        return new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sort, ""), ",")))
                .stream()
                .map(HelperReport::getOrder)
                .collect(Collectors.toList());
    }

    @Nullable
    private Specification<Organization> getSpec(TypeOrganization type,
                                                   TypeSmo smo,
                                                   List<Location> parents,
                                                   String organization,
                                                   Integer pStart,
                                                   Integer pEnd,
                                                   LogicalCondition logicalCondition,
                                                   Location[] locations) {
        List<Specification<Organization>> specs = getSpecs(type, smo, parents, organization, locations);
        Specification<Organization> mainSpec = logicalCondition == LogicalCondition.OR ?
                getSpecsWithOrCondition(specs)
                : getSpecsWithAndCondition(specs);
        return getSpecsWithAndCondition(getNecessarySpecs(pStart, pEnd)).and(mainSpec);
    }

    private List<Specification<Organization>> getSpecs(TypeOrganization type,
                                                          TypeSmo smo,
                                                          List<Location> parents,
                                                          String organization,
                                                          Location[] locations) {
        List<Specification<Organization>> result = new ArrayList<>();
        result.add(getSpecType(type));
        result.add(getSpecSmo(smo));
        result.add(getSpecParents(parents));
        result.add(getSpecOrganization(organization));
        result.add(getSpecLocations(locations));
        return result;
    }
    private Specification<Organization> getSpecType(TypeOrganization type) {
        return type != null ?
                OrganizationSpec.withType(type)
                : null;
    }

    private Specification<Organization> getSpecSmo(TypeSmo smo) {
        return smo != null ?
                OrganizationSpec.withSmo(smo)
                : null;
    }

    private Specification<Organization> getSpecOrganization(String organization) {
        return organization != null ?
                OrganizationSpec.withOrgname(organization)
                : null;
    }

    private Specification<Organization> getSpecParents(List<Location> parents) {
        return parents != null ?
                OrganizationSpec.inParent(parents)
                : null;
    }

    @Nullable
    private Specification<Organization> getSpecLocations(Location[] locations) {
        return locationsNotNull(locations) ?
                OrganizationSpec.inLocation(locations)
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

    private Specification<Organization> getSpecsWithOrCondition(List<Specification<Organization>> specs) {
        AtomicReference<Specification<Organization>> result =
                new AtomicReference<>(OrganizationSpec.FALSE_SPEC);
        specs.forEach(spec -> result.set(
                Objects.requireNonNull(result.get().or(OrganizationSpec.forOrCondition(spec)))));
        return result.get();
    }

    private Specification<Organization> getSpecsWithAndCondition(List<Specification<Organization>> specs) {
        AtomicReference<Specification<Organization>> result =
                new AtomicReference<>(OrganizationSpec.TRUE_SPEC);
        specs.forEach(spec -> result.set(
                Objects.requireNonNull(result.get().and(OrganizationSpec.forAndCondition(spec)))));
        return result.get();
    }

    private List<Specification<Organization>> getNecessarySpecs(Integer pStart, Integer pEnd) {
        List<Specification<Organization>> result = new ArrayList<>();
        result.add(getSpecPStart(pStart));
        result.add(getSpecPEnd(pEnd));
        return result;
    }

    private Specification<Organization> getSpecPEnd(Integer pEnd) {
        return pEnd != null ?
                OrganizationSpec.pEnd(pEnd)
                : null;
    }

    private Specification<Organization> getSpecPStart(Integer pStart) {
        return pStart != null ?
                OrganizationSpec.pStart(pStart)
                : null;
    }

    @NotNull
    private PaginatedList<ReportOrganizationDTO> getPListReportOrganizationDTO(Page<Organization> pData) {
        return new PaginatedList<>(
                pData.getTotalElements(),
                pData.stream()
                        .map(ReportOrganizationDTO::new)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{locationId}")
    @Secured({"ROLE_ADMIN"})
    public void remove(@PathVariable Integer locationId) {
        log.info("zzzz" + locationId);
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
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_CONTRACTOR"})
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
        item.setFunCustomer(rFunCustomer.getOne(value.getFunCustomer()));
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
        item.setFunCustomer(rFunCustomer.getOne(value.getFunCustomer()));
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
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_CONTRACTOR"})
    public List<AccessPointDetailInOrganizationDTO> apsByOrganization(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/ap", id);
        return rAccessPoints.getAllByOrganizationIdAndDeletedIsFalse(id).stream().map(AccessPointDetailInOrganizationDTO::new).collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_CONTRACTOR"})
    @PostMapping(value = "/{id}/ap/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAP(
            @PathVariable(name = "id") final Organization organization,
            @RequestBody final AccessPointNewDTO dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("->POST /api/organization/{}/ap", organization.getId());
        AccessPointDetailInOrganizationDTO bNew;
        try {
            bNew = accesspoints.giveNewCreatedAccessPoint(organization, dto, user);
            log.info("<- POST /api/organization/{}/ap/{}", organization.getId(), bNew.getId());
            return ResponseEntity.ok(bNew);
        } catch (Exception e) {
            log.error("<- POST /api/organization/{}/ap/ :: {}", organization.getId(), e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .header("Content-Type", "application/json")
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_CONTRACTOR"})
    @PutMapping(value = "/{id}/ap/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateAP(
            @PathVariable(name = "id") final Organization organization,
            @RequestBody final AccessPointNewDTO dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("->PUT /api/organization/{}/ap", organization.getId());
        AccessPointDetailInOrganizationDTO bNew;
        try {
            bNew = accesspoints.giveNewCreatedAccessPoint(organization, dto, user);
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
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_CONTRACTOR"})
    public ExternalSystemCreateStatusDTO initMonitoring(@PathVariable Integer id, @PathVariable Integer apid,
                                                        @RequestBody final MonitoringAccessPointWizardDTO wizard, @AuthenticationPrincipal User user) throws NotAllowedException {
        log.info("[{}]->GET /{}/ap/{}/init-monitoring", user.getUsername(), id, apid);
        try {
             ExternalSystemCreateStatusDTO result = sOrganization.linkAccessPointWithMonitoringSystems(id, apid, wizard, user);
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

    @GetMapping("/fun-customer/list")
    public List<FunCustomerDto> getFunCustomerList(
            @RequestParam(name = "apType", required = false) String apType
    ) {
        log.info("-> GET /api/organization/fun-customer/list");
        List<FunCustomerDto> result = sOrganization.getListFunCustomer(apType);
        log.info("<- GET /api/organization/fun-customer/list");
        return result;
    }

    @GetMapping("/fun-customer/{id}")
    public ResponseEntity<FunCustomerDto> getFunCustomer(@PathVariable Integer id) {
        log.info("-> GET /api/organization/fun-customer/{}", id);
        FunCustomerDto result;
        try {
            result = sOrganization.getFunCustomer(id);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        log.info("<- GET /api/organization/fun-customer/{}", id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/fun-customer/")
    public FunCustomerDto createFunCustomer(@RequestBody FunCustomerDto funCustomerDto) {
        log.info("-> POST /api/organization/fun-customer/");
        FunCustomerDto result = sOrganization.createOrUpdateFunCustomer(funCustomerDto);
        log.info("<- POST /api/organization/fun-customer/");
        return result;
    }

    @PutMapping("/fun-customer/")
    public FunCustomerDto updateFunCustomer(@RequestBody FunCustomerDto funCustomerDto) {
        log.info("-> PUT /api/organization/fun-customer/{}", funCustomerDto.getId());
        FunCustomerDto result = sOrganization.createOrUpdateFunCustomer(funCustomerDto);
        log.info("<- PUT /api/organization/fun-customer/{}", funCustomerDto.getId());
        return result;
    }

}
