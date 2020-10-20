package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ExtZabbixDtoResponseTriggersInTrouble {
    private String jsonrpc;
    List<Map<String, String>> result;
    private Integer id;
}
