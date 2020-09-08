package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.map.TechnicalCapabilityForLocationTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "location")
@Immutable
@Data
@org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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
                                        @NamedAttributeNode("tvOrRadioTypes"),
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
public class LocationForTable implements Serializable {

    @Id
    private Integer id;
    private String type;
    private String name;
    private Integer population;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    private LocationParent locationParent;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "key_location")
    @org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    private Set<TechnicalCapabilityForLocationTable> technicalCapabilities;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location", updatable = false)
    @org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    private Set<OrganizationForLocationTable> organizations;

    @OneToMany(mappedBy = "locationParent", fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    private List<LocationForTable> children;

    public LocationForTable() {
    }
}
