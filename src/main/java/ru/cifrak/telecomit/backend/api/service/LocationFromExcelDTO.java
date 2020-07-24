package ru.cifrak.telecomit.backend.api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;

import java.util.UUID;

@AllArgsConstructor
@Getter

public class LocationFromExcelDTO {
    private UUID fias;
    private String name;
    private Integer population;
    private String type;

    public LocationFromExcelDTO(UUID fias, String name, Integer population, String type) {
        this.fias = fias;
        this.name = name;
        this.population = population;
        this.type = type;
    }
}
