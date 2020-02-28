package ru.cifrak.telecomit.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;
import ru.cifrak.telecomit.backend.repository.RepositoryOrganization;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organization")
public class ApiOrganization {
    private RepositoryOrganization repository;

    public ApiOrganization(RepositoryOrganization repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CatalogsOrganization> list(){
        final List<CatalogsOrganization> items = repository.findAll();
        return items;
    }

    @GetMapping("/create")
    public void create() {
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
