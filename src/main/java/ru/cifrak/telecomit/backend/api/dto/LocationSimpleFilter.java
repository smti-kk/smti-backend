package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.domain.CatalogsLocation;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

@Data
@AllArgsConstructor
public class LocationSimpleFilter {
    private Integer id;

    private LocationSimpleFilter parent;

    @JsonProperty("full_name")
    private String fullName;

    private String name;

    @JsonProperty("type_location")
    private String type;

    public LocationSimpleFilter(CatalogsLocation entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.type = entity.getTypeLocation();
        this.fullName = entity.getTypeLocation() + " " + entity.getName();
        this.parent = entity.getParent() != null ? new LocationSimpleFilter(entity.getParent()): null;
    }
}
