package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Location;

@Data
public class LocationDTO {
    private Integer id;
    private String type;
    private String name;
    private String parent;
    private Integer population;


    public LocationDTO(Location location) {
        this.id = location.getId();
        this.type = location.getType();
        this.name = location.getName();
        this.parent = location.getParent() != null ? location.getParent().getType() + " " + location.getParent().getName() : null;
        this.population = location.getPopulation();
    }
}
