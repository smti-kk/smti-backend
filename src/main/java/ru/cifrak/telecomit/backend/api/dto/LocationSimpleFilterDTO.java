package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Location;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LocationSimpleFilterDTO implements Serializable {
    private Integer id;

    private String parent;

    private String fullName;

    private String name;

    private String type;

    private Integer population;

    public LocationSimpleFilterDTO(Location entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getType();
        this.population = entity.getPopulation();
        this.fullName = entity.getType() + " " + entity.getName();
        this.parent = entity.getParent() != null ? entity.getParent().getType() + " " + entity.getParent().getName() : null;
    }
}
