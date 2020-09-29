package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"locations"})

@Entity
@Table
@NamedQuery(name = "GeoData.findAll", query = "SELECT c FROM GeoData c")
public class GeoData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEODATA_ID_GENERATOR", sequenceName = "geodata_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEODATA_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;


    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(name = "administrative_center")
    private Point administrativeCenter;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(name = "border")
    @JsonProperty("geometry")
    private MultiPolygon border;

    @Column
    private UUID fias;

    @Column(length = 16)
    private String okato;

    @Column(length = 16)
    private String oktmo;

    @Column(name = "osm_id", length = 16)
    private String osmId;

    //bi-directional many-to-one association to CatalogsLocation
    @OneToMany(mappedBy = "geoData")
    private List<Location> locations;
}