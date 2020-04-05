package ru.cifrak.telecomit.backend.api.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.api.dto.TechnicalCapabilitiesDTO;
import ru.cifrak.telecomit.backend.repository.*;

@Service
@Getter
public class TechnicalCapabilitiesServiceImpl implements TechnicalCapabilitiesService {
    private RepositoryLocation repositoryLocation;

    public TechnicalCapabilitiesServiceImpl(RepositoryLocation repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
    }

    public TechnicalCapabilitiesDTO getByLocationId(Integer locationId) {
        return new TechnicalCapabilitiesDTO(repositoryLocation.getOne(locationId));
    }
}
