package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.api.dto.TechnicalCapabilitiesDTO;
import ru.cifrak.telecomit.backend.api.service.TechnicalCapabilitiesService;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/technical-capabilities")
public class ApiTechnicalCapabilities {
    private TechnicalCapabilitiesService technicalCapabilitiesService;
    private RepositoryLocation repositoryLocation;

    public ApiTechnicalCapabilities(TechnicalCapabilitiesService technicalCapabilitiesService,
                                    RepositoryLocation repositoryLocation) {
        this.technicalCapabilitiesService = technicalCapabilitiesService;
        this.repositoryLocation = repositoryLocation;
    }

    @GetMapping
    public List<TechnicalCapabilitiesDTO> getTechnicalCapabilities() {
        return repositoryLocation.findAll().stream()
                .map(TechnicalCapabilitiesDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(params = {"page", "page_size"})
    public PaginatedList<TechnicalCapabilitiesDTO> getTechnicalCapabilitiesPaginated(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize
    ) {
        Page<CatalogsLocation> locationsPage = repositoryLocation.findAll(
                PageRequest.of(page - 1, pageSize, Sort.by("id"))
        );

        List<TechnicalCapabilitiesDTO> capabilitiesDTOS = locationsPage.getContent().stream()
                .map(TechnicalCapabilitiesDTO::new)
                .collect(Collectors.toList());

        return new PaginatedList<>(locationsPage.getTotalElements(), capabilitiesDTOS);
    }

    @GetMapping("{id}")
    public TechnicalCapabilitiesDTO getTechnicalCapabilities(@PathVariable("id") Integer id) {
        return this.technicalCapabilitiesService.getByLocationId(id);
    }
}
