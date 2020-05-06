package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.Location;

@Data
@AllArgsConstructor
public class LocationSimpleFilterDTO {
    private Integer id;

    private LocationSimpleFilterDTO parent;

    @JsonProperty("full_name")
    private String fullName;

    private String name;

    @JsonProperty("type_location")
    private String type;

    public LocationSimpleFilterDTO(Location entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getType();
        this.fullName = entity.getType() + " " + entity.getName();
        this.parent = entity.getParent() != null ? new LocationSimpleFilterDTO(entity.getParent()) : null;
    }
}
