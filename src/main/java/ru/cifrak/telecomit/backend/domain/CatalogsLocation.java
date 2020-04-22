package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * The persistent class for the catalogs_location database table.
 * 
 */
@Data
@EqualsAndHashCode(exclude = {"ftcInternets", "ftcMobiles", "ftcTelevisions", "ftcRadios", "ftcPosts", "ftcAts"})
@Entity
@Table(name="catalogs_location")
@NamedQuery(name="CatalogsLocation.findAll", query="SELECT c FROM CatalogsLocation c")
@NamedEntityGraphs(value = {
		@NamedEntityGraph(
				name = CatalogsLocation.WITH_FEATURES,
				attributeNodes = {
						@NamedAttributeNode("ftcAts"),
						@NamedAttributeNode("ftcInternets"),
						@NamedAttributeNode("ftcTelevisions"),
						@NamedAttributeNode("ftcPosts"),
						@NamedAttributeNode("ftcRadios"),
						@NamedAttributeNode("ftcMobiles")
				}
		)
})
public class CatalogsLocation implements Serializable {
	public static final String WITH_FEATURES = "CatalogsLocation[FtcFeatures]";

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_LOCATION_ID_GENERATOR", sequenceName="catalogs_location_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_LOCATION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@JsonIgnore
	@Column(name="fias_code")
	private UUID fiasCode;

	@JsonIgnore
	@Column(nullable=false)
	private Integer level;

	@JsonIgnore
	@Column(nullable=false)
	private Integer lft;

	@Column(nullable=false, length=128)
	private String name;

	@JsonIgnore
	@Column(length=16)
	private String okato;

	@JsonIgnore
	@Column(length=16)
	private String oktmo;

	@Column(name="people_count", nullable=false)
	private Integer peopleCount;

	@JsonIgnore
	@Column(nullable=false)
	private Integer rght;

	@JsonIgnore
	@Column(name="tree_id", nullable=false)
	private Integer treeId;

	@JsonProperty("type_location")
	@Column(name="type_location", nullable=false, length=32)
	private String typeLocation;

	@OneToMany(mappedBy="catalogsLocation")
	@JsonManagedReference
	private List<CatalogsInfomat> catalogsInfomats;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="geo_data_id")
	private CatalogsGeolocation catalogsGeolocation;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private CatalogsLocation parent;

	@JsonIgnore
	@OneToMany(mappedBy= "parent")
	private List<CatalogsLocation> catalogsLocations;

	@OneToMany(mappedBy="catalogsLocation")
	private List<CatalogsLocationinfrastructure> catalogsLocationinfrastructures;

	@OneToMany(mappedBy = "catalogsOrganization")
	private List<CatalogsOrganization> catalogsOrganizations;

	@OneToMany(mappedBy="catalogsLocation1")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels1;

	@OneToMany(mappedBy="catalogsLocation2")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels2;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Set<FtcAts> ftcAts;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Set<FtcInternet> ftcInternets;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Set<FtcMobile> ftcMobiles;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Set<FtcPost> ftcPosts;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Set<FtcRadio> ftcRadios;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Set<FtcTelevision> ftcTelevisions;

	public CatalogsLocation() {
	}
}