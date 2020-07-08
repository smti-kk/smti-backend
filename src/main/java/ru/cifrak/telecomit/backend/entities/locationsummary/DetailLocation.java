package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.map.ShortTechnicalCapability;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "location")
@Immutable
@Getter
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "detail-locations",
                attributeNodes = {
                        @NamedAttributeNode(value = "shortTechnicalCapability", subgraph = "detail-location-tcs"),
                        @NamedAttributeNode(value = "locationParent"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "detail-location-tcs",
                                attributeNodes = {
                                        @NamedAttributeNode("trunkChannel"),
                                        @NamedAttributeNode("typeMobile"),
                                        @NamedAttributeNode("governmentDevelopmentProgram"),
                                }
                        )
                }
        )
})
public class DetailLocation {

    @Id
    private Integer id;
    private String type;
    private String name;
    private Integer population;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private LocationParent locationParent;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_location")
    private List<ShortTechnicalCapability> shortTechnicalCapability;

    public DetailLocation() {
    }

    public DetailLocation(DetailLocation detailLocation,
                          List<ShortTechnicalCapability> shortTechnicalCapability) {
        this.id = detailLocation.id;
        this.locationParent = detailLocation.locationParent;
        this.name = detailLocation.name;
        this.shortTechnicalCapability = shortTechnicalCapability;
    }
}
