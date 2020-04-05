package ru.cifrak.telecomit.backend.api.service;

import ru.cifrak.telecomit.backend.api.dto.TechnicalCapabilitiesDTO;

public interface TechnicalCapabilitiesService {
    TechnicalCapabilitiesDTO getByLocationId(Integer locationId);
}
