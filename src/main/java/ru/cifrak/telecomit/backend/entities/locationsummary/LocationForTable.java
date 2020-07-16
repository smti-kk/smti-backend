package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.map.TechnicalCapabilityForLocationTable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "location")
@Immutable
@Getter
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "detail-locations",
                attributeNodes = {
                        @NamedAttributeNode(value = "technicalCapabilities", subgraph = "detail-location-tcs"),
                        @NamedAttributeNode(value = "locationParent"),
                        @NamedAttributeNode(value = "organizations", subgraph = "access-points"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "detail-location-tcs",
                                attributeNodes = {
                                        @NamedAttributeNode("trunkChannel"),
                                        @NamedAttributeNode("typeMobile"),
                                        @NamedAttributeNode("governmentDevelopmentProgram"),
                                }
                        ),
                        @NamedSubgraph(
                                name = "access-points",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "accessPoints", subgraph = "government-program")
                                }
                        ),
                        @NamedSubgraph(
                                name = "government-program",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "governmentDevelopmentProgram")
                                }
                        )
                }
        )
})
public class LocationForTable {

    @Id
    private Integer id;
    private String type;
    private String name;
    private Integer population;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private LocationParent locationParent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location")
    private Set<TechnicalCapabilityForLocationTable> technicalCapabilities;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location")
    private Set<OrganizationForLocationTable> organizations;

    public LocationForTable() {
    }
}
