package ru.cifrak.telecomit.backend.entities.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import ru.cifrak.telecomit.backend.entities.APConnectionState;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "access_point")
@Data
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private APConnectionState connectionState;

    public MapAccessPoint(Integer id, Geometry point, APConnectionState connectionState) {
        this.id = id;
        this.point = point;
        this.connectionState = connectionState;
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
