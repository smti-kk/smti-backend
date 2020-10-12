package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoServicesParams {
    String[] output = {"serviceid", "name"};
}
