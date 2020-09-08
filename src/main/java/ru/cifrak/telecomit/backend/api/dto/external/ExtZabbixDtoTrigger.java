package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoTrigger {
    String triggerid;
    String description;
}
