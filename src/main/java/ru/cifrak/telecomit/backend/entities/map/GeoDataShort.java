package ru.cifrak.telecomit.backend.entities.map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "geo_data")
public class GeoDataShort implements Serializable {
    @Id
    @SequenceGenerator(name = "GEODATA_ID_GENERATOR", sequenceName = "geodata_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEODATA_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(name = "administrative_center")
    private Point administrativeCenter;
}
