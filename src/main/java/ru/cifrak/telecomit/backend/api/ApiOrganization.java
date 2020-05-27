package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.OrganizationDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationMoreAccessPointDTO;
import ru.cifrak.telecomit.backend.api.dto.OrganizationWithAccessPointsDTO;
import ru.cifrak.telecomit.backend.auth.service.UserService;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.repository.RepositoryAccessPoints;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@RestController
@RequestMapping("/api/organization")
public class ApiOrganization {
    private final RepositoryOrganization rOrganization;
    private final RepositoryAccessPoints rAccessPoints;

    public ApiOrganization(RepositoryOrganization repository, RepositoryAccessPoints rAccessPoints) {
        this.rOrganization = repository;
        this.rAccessPoints = rAccessPoints;
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

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Integer id, @RequestBody Organization value) throws ResourceNotFoundException {
        Organization item = rOrganization.getOne(id);
        item.setAddress(value.getAddress());
        item.setFias(value.getFias());
        item.setName(value.getName());
        item.setInn(value.getInn());
        item.setKpp(value.getKpp());
        item.setAcronym(value.getAcronym());
        item.setLocation(value.getLocation());
        //TODO: make this work later, when we have some real data...
//        item.setParent(value.getParent());
//        item.setChildren(value.getChildren());
        item.setType(value.getType());
        item.setSmo(value.getSmo());
        Organization saved = rOrganization.save(item);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/ap/")
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION"})
    public List<OrganizationMoreAccessPointDTO> apsByOrganization(@PathVariable Integer id) {
        log.info("->GET /api/organization/{}/ap", id);
        return rAccessPoints.getAllByOrganizationId(id).stream().map(OrganizationMoreAccessPointDTO::new).collect(Collectors.toList());
    }

}
