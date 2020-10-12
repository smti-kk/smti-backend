package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.Value;

@Data
public class ExtZabbixDtoResponseHostDataTrigger {
    private String triggerid;
    private String description;
}
