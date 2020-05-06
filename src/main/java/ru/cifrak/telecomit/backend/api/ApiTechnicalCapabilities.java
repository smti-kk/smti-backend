/*
package ru.cifrak.telecomit.backend.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.LocationSimple;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.api.dto.TechnicalCapabilitiesDTO;
import ru.cifrak.telecomit.backend.api.service.TechnicalCapabilitiesService;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
public class ApiTechnicalCapabilities {
    private TechnicalCapabilitiesService technicalCapabilitiesService;
    private RepositoryLocation repositoryLocation;

    public ApiTechnicalCapabilities(TechnicalCapabilitiesService technicalCapabilitiesService,
                                    RepositoryLocation repositoryLocation) {
        this.technicalCapabilitiesService = technicalCapabilitiesService;
        this.repositoryLocation = repositoryLocation;
    }

    @GetMapping("technical-capabilities")
    public List<TechnicalCapabilitiesDTO> getTechnicalCapabilities() {
        return repositoryLocation.findAll().stream()
                .map(TechnicalCapabilitiesDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "technical-capabilities", params = {"page", "page_size"})
    public PaginatedList<TechnicalCapabilitiesDTO> getTechnicalCapabilitiesPaginated(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam(name = "ordering", required = false) String ordering
    ) {
        return this.technicalCapabilitiesService.findAll(page, pageSize);
    }

    @GetMapping(value = "technical-capabilities/{id}/")
    public TechnicalCapabilitiesDTO getTechnicalCapabilities(@PathVariable("id") Integer id) {
        return this.technicalCapabilitiesService.getByLocationId(id);
    }

    @GetMapping(value = "tc-internet")
    public PaginatedList<HashMap<String, Object>> getTcInternet(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize
    ) {
        Page<Location> locationsPage = repositoryLocation.findAll(
                PageRequest.of(page - 1, pageSize, Sort.by("id"))
        );

        List<HashMap<String, Object>> capabilitiesDTOS = locationsPage.getContent().stream()
                .map(capability -> {
                    HashMap<String, Object> internetFeatures = new HashMap<>();
                    internetFeatures.put("location", new LocationSimple(capability));
//                    internetFeatures.put("internet", capability.getFtcInternets());
                    return internetFeatures;
                })
                .collect(Collectors.toList());

        return new PaginatedList<>(locationsPage.getTotalElements(), capabilitiesDTOS);
    }
}
*/
