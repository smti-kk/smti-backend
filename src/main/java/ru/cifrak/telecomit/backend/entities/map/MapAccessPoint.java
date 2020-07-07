package ru.cifrak.telecomit.backend.entities.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "access_point")
public class MapAccessPoint {
    @Id
    private Integer id;

    @JsonProperty("point")
    @JsonSerialize(using = GeometrySerializer.class)
    private Geometry point;

    @JsonIgnore
    private String type;

    @JsonIgnore
    private Date modified;

    public MapAccessPoint(Integer id, Geometry point) {
        this.id = id;
        this.point = point;
    }

    public MapAccessPoint() {
    }

    public Integer getId() {
        return id;
    }

    public Geometry getPoint() {
        return point;
    }
}
