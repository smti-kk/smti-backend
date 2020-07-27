package ru.cifrak.telecomit.backend.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class LocationFromExcelDTO {

    private final String typeMO;

    private final String nameMO;

    private final UUID fias;

    private final String name;

    private final Integer population;

    private final String type;
}
