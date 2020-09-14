package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SlaDto {
    private Long from;
    private Long to;
    private Double sla;
    private Long okTime;
    private Long problemTime;
    private Long downtimeTime;
}
