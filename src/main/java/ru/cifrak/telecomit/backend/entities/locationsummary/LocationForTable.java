package ru.cifrak.telecomit.backend.entities.locationsummary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.map.TechnicalCapabilityForLocationTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "location")
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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private LocationParent locationParent;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "key_location")
    private Set<TechnicalCapabilityForLocationTable> technicalCapabilities;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "key_location")
    private Set<OrganizationForLocationTable> organizations;

    @OneToMany(mappedBy = "locationParent", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<LocationForTable> children;

    public LocationForTable() {
    }
}
