package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoResponseTriggersData {
    String triggerid;
    String description;
}
