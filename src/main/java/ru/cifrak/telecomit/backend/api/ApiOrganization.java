package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.OrganizationDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationMoreAccessPointDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationShortDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationWithAccessPointsDTO;
import ru.cifrak.telecomit.backend.auth.service.UserService;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.service.ServiceOrganization;

import javax.transaction.Transactional;
import java.security.Principal;
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
    private final ServiceOrganization sOrganization;

    public ApiOrganization(RepositoryOrganization repository, RepositoryAccessPoints rAccessPoints, RepositoryLocation rLocation, RepositorySmoType rTypeSmo, RepositoryOrganizationType rTypeOrganization, ServiceOrganization sOrganization) {
        this.rOrganization = repository;
        this.rAccessPoints = rAccessPoints;
        this.rLocation = rLocation;
        this.rTypeSmo = rTypeSmo;
        this.rTypeOrganization = rTypeOrganization;
        this.sOrganization = sOrganization;
    }

    @GetMapping
    public List<Organization> list() {
        return rOrganization.findAll();
    }

    @GetMapping(params = "location")
    public List<OrganizationWithAccessPointsDTO> listByLocationId(@RequestParam("location") Integer locationId) {
        return rOrganization.findAllByLocationId(locationId).stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public OrganizationDTO item(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/", id);
        return rOrganization.findById(id).map(OrganizationDTO::new).orElse(null);
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public Organization createItem(Principal principal, @RequestBody Organization item) {
        log.info("->POST /api/organization/ ");
        final User user = UserService.getUser(principal);
        return rOrganization.saveAndFlush(item);
    }


    @Transactional
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
    public List<OrganizationMoreAccessPointDTO> apsByOrganization(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/ap", id);
        return rAccessPoints.getAllByOrganizationId(id).stream().map(OrganizationMoreAccessPointDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}/ap/{apid}/init-monitoring")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public ResponseEntity<String> initMonitoring(@PathVariable Integer id, @PathVariable Integer apid) {
        log.info("->GET /{}/ap/{}/init-monitoring", id, apid);
        try {
            sOrganization.initializeMonitoringOnAp(id, apid);
            log.info("<-GET /{}/ap/{}/init-monitoring", id, apid);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json")
                    .body("{\"result\": \"access point enabled in monitoring system\"}");
        } catch (Exception e) {
            log.warn("->GET /{}/ap/{}/init-monitoring :EXCEPTION {}", id, apid, e.getMessage());
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json")
                    .body("{\"result\": \"error: access point NOT enabled in monitoring system due: " + e.getMessage() + "\"}");
        }
    }

}
