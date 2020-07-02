package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MonitoringAccessPointWizardDTO {
    private ZabbixDTO device;
    private ZabbixDTO sensor;
}
