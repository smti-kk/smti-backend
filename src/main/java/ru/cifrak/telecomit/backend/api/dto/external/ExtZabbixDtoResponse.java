package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtZabbixDtoResponse {
    private String jsonrpc;
    private Object result;
    private Integer id;
}
