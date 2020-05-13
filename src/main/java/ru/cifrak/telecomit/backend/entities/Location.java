package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the catalogs_location database table.
 */
@Data
@NoArgsConstructor
//@EqualsAndHashCode(exclude = {"ftcInternets", "ftcMobiles", "ftcTelevisions", "ftcRadios", "ftcPosts", "ftcAts"})
@EqualsAndHashCode(exclude = {"geoData", "children", "parent"})

@Entity
@Table
@NamedQuery(name = "Location.findAll", query = "SELECT c FROM Location c")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = Location.WITH_FEATURES,
                attributeNodes = {
                        @NamedAttributeNode(value = "geoData"),
                        @NamedAttributeNode(value = "parent"),
                        /*@NamedAttributeNode(value = "tcAts", subgraph = "opers"),
                        @NamedAttributeNode(value = "tcInternets", subgraph = "opers"),
                        @NamedAttributeNode(value = "tcTvs", subgraph = "opers"),
                        @NamedAttributeNode(value = "tcPosts", subgraph = "opers"),
                        @NamedAttributeNode(value = "tcRadios", subgraph = "opers"),
                        @NamedAttributeNode(value = "tcMobiles", subgraph = "opers")*/
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "opers",
                                attributeNodes = {
                                        @NamedAttributeNode("catalogsOperator")
                                }
                        )
                }

        )/*,
        //TODO: TELECOMIT-155 !!!WIP!!! proper select
        @NamedEntityGraph(
                name = Location.WITH_ORGANIZATIONS_ACCESSPOINTS,
                attributeNodes = {
                        @NamedAttributeNode(value = "organizations", subgraph="access_points"),
                },
				subgraphs = {
						@NamedSubgraph(
								name = "access_points",
								attributeNodes = {
										@NamedAttributeNode("monitoringAccesspointRes")
								}
						)
				}

        )*/
}
)
public class Location implements Serializable {
    public static final String WITH_FEATURES = "Location[FtcFeatures]";
    public static final String WITH_ORGANIZATIONS_ACCESSPOINTS = "Location[Organization]";

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "LOCATION_ID_GENERATOR", sequenceName = "location_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column
    private UUID fias;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 16)
    private String okato;

    @Column(length = 16)
    private String oktmo;

    @Column(nullable = false)
    private Integer population;

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @JsonIgnoreProperties({"location"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geo_data_id")
    private GeoData geoData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Location parent;

    @OneToMany(mappedBy = "parent")
    private List<Location> children;
/*
    @OneToMany(mappedBy = "location")
    private List<Organization> organizations;

    @OneToMany(mappedBy = "locationStart")
    private List<TrunkChannel> trunkChannelsStart;

    @OneToMany(mappedBy = "locationend")
    private List<TrunkChannel> trunkChannelsEnd;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Set<TcAts> tcAts;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Set<TcInternet> tcInternets;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Set<TcMobile> tcMobiles;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Set<TcPost> tcPosts;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Set<TcRadio> tcRadios;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Set<TcTv> tcTvs;*/

}