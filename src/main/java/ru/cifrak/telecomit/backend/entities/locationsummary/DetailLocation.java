package ru.cifrak.telecomit.backend.entities.locationsummary;

import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.map.ShortTechnicalCapability;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "location")
@Immutable
public class DetailLocation {
    @Id
    private Integer id;
    private String type;
    private String name;
    private Integer population;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private LocationParent locationParent;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_location")
    private List<ShortTechnicalCapability> shortTechnicalCapability;
}
