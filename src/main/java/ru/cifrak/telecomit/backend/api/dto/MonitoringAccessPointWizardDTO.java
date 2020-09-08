package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cifrak.telecomit.backend.api.dto.external.ExtZabbixDto;

@Data
@NoArgsConstructor

public class MonitoringAccessPointWizardDTO {
    private String networks;
    private ExtZabbixDto device;
    private ExtZabbixDto sensor;
}
