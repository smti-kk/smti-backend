package ru.cifrak.telecomit.backend.entities.map;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "location")
public class MapLocation {
    @Id
    private Integer id;

    @ManyToOne
    private GeoDataShort geoData;
    private String name;
    private String type;

    @Column
    private UUID fias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private LocationParent parent;

    public MapLocation() {
    }
}
