package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ZabbixDTO {
    private String hostName;
    private String ip;
    private String groupid;
    private String tag, tagValue;
    private String templateid;
    private String macro, macroValue;
}
