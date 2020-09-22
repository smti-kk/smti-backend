package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
@NoArgsConstructor
public class ExtZabbixDtoResponseSla {
    private String jsonrpc;
    private Map<String, ExtZabbixDtoResponseSlaData> result;
    private Integer id;
}
