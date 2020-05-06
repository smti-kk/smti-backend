/*
package ru.cifrak.telecomit.backend.api.service;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.api.dto.PaginatedList;
import ru.cifrak.telecomit.backend.api.dto.TechnicalCapabilitiesDTO;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.repository.RepositoryLocation;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
public class TechnicalCapabilitiesServiceImpl implements TechnicalCapabilitiesService {
    private RepositoryLocation repositoryLocation;

    public TechnicalCapabilitiesServiceImpl(RepositoryLocation repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
    }

    public PaginatedList<TechnicalCapabilitiesDTO> findAll(Integer page, Integer pageSize) {
        Page<Location> locationsPage = repositoryLocation.findAll(PageRequest.of(page - 1, pageSize));
        List<TechnicalCapabilitiesDTO> capabilitiesDTOS = locationsPage.getContent().stream()
                .map(TechnicalCapabilitiesDTO::new)
                .collect(Collectors.toList());
        return new PaginatedList<>(locationsPage.getTotalElements(), capabilitiesDTOS);
    }

    public TechnicalCapabilitiesDTO getByLocationId(Integer locationId) {
        return new TechnicalCapabilitiesDTO(repositoryLocation.get(locationId));
    }
}
*/
