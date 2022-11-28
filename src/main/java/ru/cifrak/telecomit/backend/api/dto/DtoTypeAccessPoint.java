package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DtoTypeAccessPoint {

    ESPD("ESPD", "ЕСПД"),
    SMO("SMO", "СЗО");

    private String key;
    private String value;

}
