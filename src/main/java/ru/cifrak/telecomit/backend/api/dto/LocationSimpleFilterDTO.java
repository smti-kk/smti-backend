package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Location;

@Data
@AllArgsConstructor
public class LocationSimpleFilterDTO {
    private Integer id;

    @JsonIgnoreProperties("parent")
    private LocationSimpleFilterDTO parent;

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
        this.parent = entity.getParent() != null ? new LocationSimpleFilterDTO(entity.getParent()) : null;
    }
}
