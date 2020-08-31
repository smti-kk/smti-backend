package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoGetTrgiggerParams {
    String hostids;
    String output [] =  {"triggerid","description"};

    public ExtZabbixDtoGetTrgiggerParams(String hostids) {
        this.hostids = hostids;
    }
}
