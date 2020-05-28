package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

@Data
@AllArgsConstructor
public class LocationSimple {
    private Integer id;

    private String parent;
    private Integer infomat;

    private String fullName;

    private String type;

    private Integer population;

    @JsonProperty("geo_data")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point geoData;

    private String name;


    public LocationSimple(Location entity) {
        this.id = entity.getId();
        this.fullName = entity.getType() + " " + entity.getName();
        this.name = entity.getName();
        this.type = entity.getType();
        this.geoData = entity.getGeoData() != null
                ? entity.getGeoData().getAdministrativeCenter()
                : null;
        this.parent = entity.getParent() != null ? entity.getParent().getType() + " " + entity.getParent().getName() : null;
        this.population = entity.getPopulation();
//        this.infomat = entity.getInfomats().size();
    }
}
