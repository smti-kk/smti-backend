package ru.cifrak.telecomit.backend.api.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ExtZabbixDtoResponseTriggers {
    private String jsonrpc;
    private List<ExtZabbixDtoResponseTriggersData> result;
    private Integer id;

    @SuppressWarnings("unchecked")
    @JsonProperty("result")
    private void unpack(List<Map<String, String>> result) {
        this.result = result.stream().map(item -> new ExtZabbixDtoResponseTriggersData(item.get("triggerid"),item.get("description"))).collect(Collectors.toList());
    }
}
