package ru.cifrak.telecomit.backend.api.dto.external;

import lombok.Value;

@Value
public class ExtZabbixDtoService {
    String name;
    Long algorithm;
    Integer showsla;
    Double goodsla;
    Integer sortorder;
    Long parentid;
}
