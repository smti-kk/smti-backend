package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.MultiPolygon;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

@Data
@AllArgsConstructor
public class LocationAreaBorders {
    private Integer id;
    private String name;
    private String fullName;
    private String type = "Feature";

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private MultiPolygon geometry;

    public LocationAreaBorders(Location entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.fullName = entity.getType() + " " + entity.getName();
        this.geometry = entity.getGeoData().getBorder();
    }
}
