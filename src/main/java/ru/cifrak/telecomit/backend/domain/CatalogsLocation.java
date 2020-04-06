package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the catalogs_location database table.
 * 
 */
@Data
@Entity
@Table(name="catalogs_location")
@NamedQuery(name="CatalogsLocation.findAll", query="SELECT c FROM CatalogsLocation c")
@Filters({
		@Filter(name="-name", condition="order by name DESC")
})
public class CatalogsLocation implements Serializable {
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

	//bi-directional many-to-one association to CatalogsInfomat
	@OneToMany(mappedBy="catalogsLocation")
	@JsonManagedReference
	private List<CatalogsInfomat> catalogsInfomats;
/*
	//bi-directional many-to-one association to CapabilitiesClarifypetition
	@OneToMany(mappedBy="catalogsLocation")
	private List<CapabilitiesClarifypetition> capabilitiesClarifypetitions;

	//bi-directional many-to-one association to CapabilitiesPetition
	@OneToMany(mappedBy="catalogsLocation")
	private List<CapabilitiesPetition> capabilitiesPetitions;

	//bi-directional many-to-one association to CatalogsInfomat
	@OneToMany(mappedBy="catalogsLocation")
	private List<CatalogsInfomat> catalogsInfomats;
*/
	//bi-directional many-to-one association to CatalogsGeolocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="geo_data_id")
	private CatalogsGeolocation catalogsGeolocation;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private CatalogsLocation parent;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsLocation
	@OneToMany(mappedBy= "parent")
	private List<CatalogsLocation> catalogsLocations;

	//bi-directional many-to-one association to CatalogsLocationinfrastructure
	@OneToMany(mappedBy="catalogsLocation")
	private List<CatalogsLocationinfrastructure> catalogsLocationinfrastructures;

	//bi-directional many-to-one association to CatalogsOrganization
	@OneToMany(mappedBy = "catalogsOrganization")
	private List<CatalogsOrganization> catalogsOrganizations;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@OneToMany(mappedBy="catalogsLocation1")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels1;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@OneToMany(mappedBy="catalogsLocation2")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels2;

	//bi-directional many-to-one association to FtcAts
	@OneToMany(mappedBy="catalogsLocation")
	private List<FtcAts> ftcAts;

	//bi-directional many-to-one association to FtcInternet
	@OneToMany(mappedBy="catalogsLocation")
	private List<FtcInternet> ftcInternets;

	//bi-directional many-to-one association to FtcMobile
	@OneToMany(mappedBy="catalogsLocation")
	private List<FtcMobile> ftcMobiles;

	//bi-directional many-to-one association to FtcPost
	@OneToMany(mappedBy="catalogsLocation")
	private List<FtcPost> ftcPosts;

	//bi-directional many-to-one association to FtcRadio
	@OneToMany(mappedBy="catalogsLocation")
	private List<FtcRadio> ftcRadios;

	//bi-directional many-to-one association to FtcTelevision
	@OneToMany(mappedBy="catalogsLocation")
	private List<FtcTelevision> ftcTelevisions;
/*
	//bi-directional many-to-one association to UserInfoUserLocation
	@OneToMany(mappedBy="catalogsLocation")
	private List<UserInfoUserLocation> userInfoUserLocations;
*/
	public CatalogsLocation() {
	}
}