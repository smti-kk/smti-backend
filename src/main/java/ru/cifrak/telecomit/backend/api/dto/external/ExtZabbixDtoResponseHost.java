package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ExtZabbixDtoResponseHost {
    private String jsonrpc;
    private List<ExtZabbixDtoResponseHostData> result;
    private Integer id;
}
