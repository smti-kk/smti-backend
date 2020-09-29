package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;

import java.util.List;

@Data
public class ExtZabbixDtoResponseHostData {
    String hostid;
    String host;

    List<ExtZabbixDtoResponseHostDataTrigger> triggers;
    List<ExtZabbixDtoResponseHostDataInterface> interfaces;
}
