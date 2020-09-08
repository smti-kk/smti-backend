package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ExtZabbixDto {
    private String hostName;
    private String ip;
    private String groupid;
    private String tag, tagValue;
    private String templateid;
    private String macro, macroValue;
}
