package ru.cifrak.telecomit.backend.api.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ExtZabbixDtoResponseService {
    private String jsonrpc;
    private List<String> result;
    private Integer id;

    @SuppressWarnings("unchecked")
    @JsonProperty("result")
    private void unpack(Map<String, List<String>> result) {
        this.result = result.get("serviceids");
    }
}
