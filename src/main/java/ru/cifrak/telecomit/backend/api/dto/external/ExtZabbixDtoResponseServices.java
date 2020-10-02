package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ExtZabbixDtoResponseServices {
    private String jsonrpc;
    List<ExtZabbixDtoResponseServicesData> result;
    private Integer id;
}
