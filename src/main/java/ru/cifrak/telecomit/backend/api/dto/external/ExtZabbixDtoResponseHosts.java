package ru.cifrak.telecomit.backend.api.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
public class ExtZabbixDtoResponseHosts {
    private String jsonrpc;
    private List<ExtZabbixDtoResponseHostsData> result;
    private Integer id;
}
