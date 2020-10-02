package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.Value;

@Data
public class ExtZabbixDtoResponseServicesData {
    String serviceid;
    String name;
}
