package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.*;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.ServiceAccessPoint;
import ru.cifrak.telecomit.backend.service.ServiceOrganization;

import javax.transaction.Transactional;
import java.util.List;
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

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public List<Organization> list() {
        return rOrganization.findAll();
    }

    @GetMapping(params = "location")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public List<OrganizationWithAccessPointsDTO> listByLocationId(@RequestParam("location") Integer locationId) {
        return rOrganization.findAllByLocationId(locationId).stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList());
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
    public ResponseEntity<OrganizationShortDTO> createItem(@RequestBody OrganizationShortDTO value) {
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
        item.setType(rTypeOrganization.getOne(value.getType()));
        item.setSmo(rTypeSmo.getOne(value.getSmo()));
        Organization saved = rOrganization.save(item);
        log.info("<-PUT /api/organization/{}", item.getId());
        return ResponseEntity.ok(new OrganizationDTO(saved));
    }

    @GetMapping("/{id}/ap/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public List<AccessPointDetailInOrganizationDTO> apsByOrganization(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/ap", id);
        return rAccessPoints.getAllByOrganizationId(id).stream().map(AccessPointDetailInOrganizationDTO::new).collect(Collectors.toList());
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

    @PostMapping("/{id}/ap/{apid}/init-monitoring")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public ResponseEntity<String> initMonitoring(@PathVariable Integer id, @PathVariable Integer apid,
                                                 @RequestBody final MonitoringAccessPointWizardDTO wizard) {
        log.info("->GET /{}/ap/{}/init-monitoring", id, apid);
        try {
            sOrganization.initializeMonitoringOnAp(id, apid, wizard);
            log.info("<-GET /{}/ap/{}/init-monitoring", id, apid);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json")
                    .body("{\"result\": \"access point enabled in monitoring system\"}");
        } catch (Exception e) {
            log.warn("<-GET /{}/ap/{}/init-monitoring :EXCEPTION {}", id, apid, e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .header("Content-Type", "application/json")
                    .body("{\"error\": \"access point NOT enabled in monitoring system due: " + e.getMessage() + "\"}");
        }
    }
}
