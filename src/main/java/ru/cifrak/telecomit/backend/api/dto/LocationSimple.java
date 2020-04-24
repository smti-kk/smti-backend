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
public class LocationSimple {
    private Integer id;

    private String parent;
    private Integer infomat;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("type_location")
    private String type;

    @JsonProperty("people_count")
    private Integer peopleCount;

    @JsonProperty("geo_data")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point geoData;

    private String name;


    public LocationSimple(CatalogsLocation entity) {
        this.id = entity.getId();
        this.fullName = entity.getTypeLocation() + " " + entity.getName();
        this.name = entity.getName();
        this.type = entity.getTypeLocation();
        this.geoData = entity.getCatalogsGeolocation() != null
                ? entity.getCatalogsGeolocation().getAdministrativeCenter()
                : null;
        this.parent = entity.getParent() != null ? entity.getParent().getTypeLocation() + " " + entity.getParent().getName() : null;
        this.peopleCount = entity.getPeopleCount();
        this.infomat = entity.getCatalogsInfomats().size();
    }
}
