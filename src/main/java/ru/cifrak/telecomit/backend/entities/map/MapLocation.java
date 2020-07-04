package ru.cifrak.telecomit.backend.entities.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

public class MapLocation {
    private final Integer id;

    @JsonProperty("point")
    @JsonSerialize(using = GeometrySerializer.class)
    private final Geometry point;

    public MapLocation(Integer id, Geometry point) {
        this.id = id;
        this.point = point;
    }

    public Integer getId() {
        return id;
    }

    public Geometry getPoint() {
        return point;
    }
}
