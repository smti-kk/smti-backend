package ru.cifrak.telecomit.backend.features.comparing;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.cifrak.telecomit.backend.entities.locationsummary.LocationParent;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "location")
public class LocationFC implements Serializable {
    @Id
    private Integer id;
    private String type;
    private String name;
    private Integer population;
    private String okato;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private LocationParent locationParent;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "key_location")
    private List<LocationFeatureTc> technicalCapabilities;
}
