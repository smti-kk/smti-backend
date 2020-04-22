package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;

@Data
@AllArgsConstructor
public class LocationReportOrganization {
    private Integer id;
    private String name;

    @JsonProperty("type_location")
    private String type;

    private Integer level;

    public LocationReportOrganization(CatalogsLocation entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getTypeLocation();
        this.level = entity.getLevel();
    }
}
