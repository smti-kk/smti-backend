package ru.cifrak.telecomit.backend.entities.locationsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.cifrak.telecomit.backend.entities.GeoData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "location")
@Data
@org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LocationForReference implements Serializable {
    @Id
    private Integer id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false)
    private Integer population;

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private LocationParent locationParent;

    @Column
    private UUID fias;

    @Column(length = 16)
    private String okato;

    @Column(length = 16)
    private String oktmo;

//    @JsonIgnoreProperties({"location"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "geo_data_id")
    private GeoData geoData;

    public LocationForReference() {
    }
}
