package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.domain.CatalogsOrganization;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class LocationSimple {
    public LocationSimple(CatalogsLocation entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getTypeLocation();
    }

    private Integer id;
    private String name;

    @JsonProperty("type_location")
    private String type;

}
