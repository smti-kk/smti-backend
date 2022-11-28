package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeAccessPoint {

    ESPD("ESPD", "ЕСПД"),
    SMO("SMO", "СЗО");

    private String key;
    private String value;

}
