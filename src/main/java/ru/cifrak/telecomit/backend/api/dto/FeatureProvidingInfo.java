package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;

@Data
public class FeatureProvidingInfo {
    private Integer locationsCount;
    private Integer population;
    private Integer locationsPercents;
    private Integer populationPercents;

    public FeatureProvidingInfo(Integer locationsCount,
                                Integer population,
                                Integer locationsPercents,
                                Integer populationPercents) {
        this.locationsCount = locationsCount;
        this.population = population;
        this.locationsPercents = locationsPercents;
        this.populationPercents = populationPercents;
    }
}
