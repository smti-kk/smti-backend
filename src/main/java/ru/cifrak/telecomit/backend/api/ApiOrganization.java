package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.OrganizationWithAccessPointsDTO;
import ru.cifrak.telecomit.backend.entities.Organization;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organization")
public class ApiOrganization {
    private RepositoryOrganization repository;

    public ApiOrganization(RepositoryOrganization repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Organization> list() {
        return repository.findAll();
    }

    @GetMapping(params = "location")
    public List<OrganizationWithAccessPointsDTO> listByLocationId(@RequestParam("location") Integer locationId) {
        return repository.findAllByLocationId(locationId).stream()
                .map(OrganizationWithAccessPointsDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/create")
    public void create() {
        //TODO: this workable! but make it to business correct
//        final CatalogsOrganization CatalogsOrganization = CatalogsOrganization.builder()
//                .address("address")
//                .fias(UUID.randomUUID())
//                .fullName("asdasd")
//                .inn(1)
//                .kpp(122)
//                .locationId(1234)
//                .name("qwe")
//                .build();
//        repository.save(CatalogsOrganization);
    }
}
