package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.MultiPolygon;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

@Data
@AllArgsConstructor
public class LocationAreaBorders {
    private Integer id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("border_geojson")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private MultiPolygon type;

    public LocationAreaBorders(CatalogsLocation entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.fullName = entity.getTypeLocation() + " " + entity.getName();
        this.type = entity.getCatalogsGeolocation().getBorder();
    }
}
