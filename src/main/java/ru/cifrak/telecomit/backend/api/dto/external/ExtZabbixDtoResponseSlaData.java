package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExtZabbixDtoResponseSlaData {
    private String status;
    private List<List<Object>> problems;
    private List<SlaDto> sla;
}
