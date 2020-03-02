package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the catalogs_location database table.
 * 
 */
@Entity
@Table(name="catalogs_location")
@NamedQuery(name="CatalogsLocation.findAll", query="SELECT c FROM CatalogsLocation c")
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
/*
	//bi-directional many-to-one association to CatalogsOrganization
	@OneToMany(mappedBy="catalogsLocation")
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

	//bi-directional many-to-one association to UserInfoUserLocation
	@OneToMany(mappedBy="catalogsLocation")
	private List<UserInfoUserLocation> userInfoUserLocations;
*/
	public CatalogsLocation() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UUID getFiasCode() {
		return this.fiasCode;
	}

	public void setFiasCode(UUID fiasCode) {
		this.fiasCode = fiasCode;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLft() {
		return this.lft;
	}

	public void setLft(Integer lft) {
		this.lft = lft;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOkato() {
		return this.okato;
	}

	public void setOkato(String okato) {
		this.okato = okato;
	}

	public String getOktmo() {
		return this.oktmo;
	}

	public void setOktmo(String oktmo) {
		this.oktmo = oktmo;
	}

	public Integer getPeopleCount() {
		return this.peopleCount;
	}

	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}

	public Integer getRght() {
		return this.rght;
	}

	public void setRght(Integer rght) {
		this.rght = rght;
	}

	public Integer getTreeId() {
		return this.treeId;
	}

	public void setTreeId(Integer treeId) {
		this.treeId = treeId;
	}

	public String getTypeLocation() {
		return this.typeLocation;
	}

	public void setTypeLocation(String typeLocation) {
		this.typeLocation = typeLocation;
	}
/*
	public List<CapabilitiesClarifypetition> getCapabilitiesClarifypetitions() {
		return this.capabilitiesClarifypetitions;
	}

	public void setCapabilitiesClarifypetitions(List<CapabilitiesClarifypetition> capabilitiesClarifypetitions) {
		this.capabilitiesClarifypetitions = capabilitiesClarifypetitions;
	}

	public CapabilitiesClarifypetition addCapabilitiesClarifypetition(CapabilitiesClarifypetition capabilitiesClarifypetition) {
		getCapabilitiesClarifypetitions().add(capabilitiesClarifypetition);
		capabilitiesClarifypetition.setCatalogsLocation(this);

		return capabilitiesClarifypetition;
	}

	public CapabilitiesClarifypetition removeCapabilitiesClarifypetition(CapabilitiesClarifypetition capabilitiesClarifypetition) {
		getCapabilitiesClarifypetitions().remove(capabilitiesClarifypetition);
		capabilitiesClarifypetition.setCatalogsLocation(null);

		return capabilitiesClarifypetition;
	}

	public List<CapabilitiesPetition> getCapabilitiesPetitions() {
		return this.capabilitiesPetitions;
	}

	public void setCapabilitiesPetitions(List<CapabilitiesPetition> capabilitiesPetitions) {
		this.capabilitiesPetitions = capabilitiesPetitions;
	}

	public CapabilitiesPetition addCapabilitiesPetition(CapabilitiesPetition capabilitiesPetition) {
		getCapabilitiesPetitions().add(capabilitiesPetition);
		capabilitiesPetition.setCatalogsLocation(this);

		return capabilitiesPetition;
	}

	public CapabilitiesPetition removeCapabilitiesPetition(CapabilitiesPetition capabilitiesPetition) {
		getCapabilitiesPetitions().remove(capabilitiesPetition);
		capabilitiesPetition.setCatalogsLocation(null);

		return capabilitiesPetition;
	}

	public List<CatalogsInfomat> getCatalogsInfomats() {
		return this.catalogsInfomats;
	}

	public void setCatalogsInfomats(List<CatalogsInfomat> catalogsInfomats) {
		this.catalogsInfomats = catalogsInfomats;
	}

	public CatalogsInfomat addCatalogsInfomat(CatalogsInfomat catalogsInfomat) {
		getCatalogsInfomats().add(catalogsInfomat);
		catalogsInfomat.setCatalogsLocation(this);

		return catalogsInfomat;
	}

	public CatalogsInfomat removeCatalogsInfomat(CatalogsInfomat catalogsInfomat) {
		getCatalogsInfomats().remove(catalogsInfomat);
		catalogsInfomat.setCatalogsLocation(null);

		return catalogsInfomat;
	}
*/
	public CatalogsGeolocation getCatalogsGeolocation() {
		return this.catalogsGeolocation;
	}

	public void setCatalogsGeolocation(CatalogsGeolocation catalogsGeolocation) {
		this.catalogsGeolocation = catalogsGeolocation;
	}

	public CatalogsLocation getParent() {
		return this.parent;
	}

	public void setParent(CatalogsLocation catalogsLocation) {
		this.parent = catalogsLocation;
	}

	public List<CatalogsLocation> getCatalogsLocations() {
		return this.catalogsLocations;
	}

	public void setCatalogsLocations(List<CatalogsLocation> catalogsLocations) {
		this.catalogsLocations = catalogsLocations;
	}

	public CatalogsLocation addCatalogsLocation(CatalogsLocation catalogsLocation) {
		getCatalogsLocations().add(catalogsLocation);
		catalogsLocation.setParent(this);

		return catalogsLocation;
	}

	public CatalogsLocation removeCatalogsLocation(CatalogsLocation catalogsLocation) {
		getCatalogsLocations().remove(catalogsLocation);
		catalogsLocation.setParent(null);

		return catalogsLocation;
	}

	public List<CatalogsLocationinfrastructure> getCatalogsLocationinfrastructures() {
		return this.catalogsLocationinfrastructures;
	}

	public void setCatalogsLocationinfrastructures(List<CatalogsLocationinfrastructure> catalogsLocationinfrastructures) {
		this.catalogsLocationinfrastructures = catalogsLocationinfrastructures;
	}

	public CatalogsLocationinfrastructure addCatalogsLocationinfrastructure(CatalogsLocationinfrastructure catalogsLocationinfrastructure) {
		getCatalogsLocationinfrastructures().add(catalogsLocationinfrastructure);
		catalogsLocationinfrastructure.setCatalogsLocation(this);

		return catalogsLocationinfrastructure;
	}

	public CatalogsLocationinfrastructure removeCatalogsLocationinfrastructure(CatalogsLocationinfrastructure catalogsLocationinfrastructure) {
		getCatalogsLocationinfrastructures().remove(catalogsLocationinfrastructure);
		catalogsLocationinfrastructure.setCatalogsLocation(null);

		return catalogsLocationinfrastructure;
	}
/*
	public List<CatalogsOrganization> getCatalogsOrganizations() {
		return this.catalogsOrganizations;
	}

	public void setCatalogsOrganizations(List<CatalogsOrganization> catalogsOrganizations) {
		this.catalogsOrganizations = catalogsOrganizations;
	}

	public CatalogsOrganization addCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		getCatalogsOrganizations().add(catalogsOrganization);
		catalogsOrganization.setCatalogsLocation(this);

		return catalogsOrganization;
	}

	public CatalogsOrganization removeCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		getCatalogsOrganizations().remove(catalogsOrganization);
		catalogsOrganization.setCatalogsLocation(null);

		return catalogsOrganization;
	}

	public List<CatalogsTrunkchannel> getCatalogsTrunkchannels1() {
		return this.catalogsTrunkchannels1;
	}

	public void setCatalogsTrunkchannels1(List<CatalogsTrunkchannel> catalogsTrunkchannels1) {
		this.catalogsTrunkchannels1 = catalogsTrunkchannels1;
	}

	public CatalogsTrunkchannel addCatalogsTrunkchannels1(CatalogsTrunkchannel catalogsTrunkchannels1) {
		getCatalogsTrunkchannels1().add(catalogsTrunkchannels1);
		catalogsTrunkchannels1.setCatalogsLocation1(this);

		return catalogsTrunkchannels1;
	}

	public CatalogsTrunkchannel removeCatalogsTrunkchannels1(CatalogsTrunkchannel catalogsTrunkchannels1) {
		getCatalogsTrunkchannels1().remove(catalogsTrunkchannels1);
		catalogsTrunkchannels1.setCatalogsLocation1(null);

		return catalogsTrunkchannels1;
	}

	public List<CatalogsTrunkchannel> getCatalogsTrunkchannels2() {
		return this.catalogsTrunkchannels2;
	}

	public void setCatalogsTrunkchannels2(List<CatalogsTrunkchannel> catalogsTrunkchannels2) {
		this.catalogsTrunkchannels2 = catalogsTrunkchannels2;
	}

	public CatalogsTrunkchannel addCatalogsTrunkchannels2(CatalogsTrunkchannel catalogsTrunkchannels2) {
		getCatalogsTrunkchannels2().add(catalogsTrunkchannels2);
		catalogsTrunkchannels2.setCatalogsLocation2(this);

		return catalogsTrunkchannels2;
	}

	public CatalogsTrunkchannel removeCatalogsTrunkchannels2(CatalogsTrunkchannel catalogsTrunkchannels2) {
		getCatalogsTrunkchannels2().remove(catalogsTrunkchannels2);
		catalogsTrunkchannels2.setCatalogsLocation2(null);

		return catalogsTrunkchannels2;
	}

	public List<FtcAts> getFtcAts() {
		return this.ftcAts;
	}

	public void setFtcAts(List<FtcAts> ftcAts) {
		this.ftcAts = ftcAts;
	}

	public FtcAts addFtcAts(FtcAts ftcAts) {
		getFtcAts().add(ftcAts);
		ftcAts.setCatalogsLocation(this);

		return ftcAts;
	}

	public FtcAts removeFtcAts(FtcAts ftcAts) {
		getFtcAts().remove(ftcAts);
		ftcAts.setCatalogsLocation(null);

		return ftcAts;
	}

	public List<FtcInternet> getFtcInternets() {
		return this.ftcInternets;
	}

	public void setFtcInternets(List<FtcInternet> ftcInternets) {
		this.ftcInternets = ftcInternets;
	}

	public FtcInternet addFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().add(ftcInternet);
		ftcInternet.setCatalogsLocation(this);

		return ftcInternet;
	}

	public FtcInternet removeFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().remove(ftcInternet);
		ftcInternet.setCatalogsLocation(null);

		return ftcInternet;
	}

	public List<FtcMobile> getFtcMobiles() {
		return this.ftcMobiles;
	}

	public void setFtcMobiles(List<FtcMobile> ftcMobiles) {
		this.ftcMobiles = ftcMobiles;
	}

	public FtcMobile addFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().add(ftcMobile);
		ftcMobile.setCatalogsLocation(this);

		return ftcMobile;
	}

	public FtcMobile removeFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().remove(ftcMobile);
		ftcMobile.setCatalogsLocation(null);

		return ftcMobile;
	}

	public List<FtcPost> getFtcPosts() {
		return this.ftcPosts;
	}

	public void setFtcPosts(List<FtcPost> ftcPosts) {
		this.ftcPosts = ftcPosts;
	}

	public FtcPost addFtcPost(FtcPost ftcPost) {
		getFtcPosts().add(ftcPost);
		ftcPost.setCatalogsLocation(this);

		return ftcPost;
	}

	public FtcPost removeFtcPost(FtcPost ftcPost) {
		getFtcPosts().remove(ftcPost);
		ftcPost.setCatalogsLocation(null);

		return ftcPost;
	}

	public List<FtcRadio> getFtcRadios() {
		return this.ftcRadios;
	}

	public void setFtcRadios(List<FtcRadio> ftcRadios) {
		this.ftcRadios = ftcRadios;
	}

	public FtcRadio addFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().add(ftcRadio);
		ftcRadio.setCatalogsLocation(this);

		return ftcRadio;
	}

	public FtcRadio removeFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().remove(ftcRadio);
		ftcRadio.setCatalogsLocation(null);

		return ftcRadio;
	}

	public List<FtcTelevision> getFtcTelevisions() {
		return this.ftcTelevisions;
	}

	public void setFtcTelevisions(List<FtcTelevision> ftcTelevisions) {
		this.ftcTelevisions = ftcTelevisions;
	}

	public FtcTelevision addFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().add(ftcTelevision);
		ftcTelevision.setCatalogsLocation(this);

		return ftcTelevision;
	}

	public FtcTelevision removeFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().remove(ftcTelevision);
		ftcTelevision.setCatalogsLocation(null);

		return ftcTelevision;
	}

	public List<UserInfoUserLocation> getUserInfoUserLocations() {
		return this.userInfoUserLocations;
	}

	public void setUserInfoUserLocations(List<UserInfoUserLocation> userInfoUserLocations) {
		this.userInfoUserLocations = userInfoUserLocations;
	}

	public UserInfoUserLocation addUserInfoUserLocation(UserInfoUserLocation userInfoUserLocation) {
		getUserInfoUserLocations().add(userInfoUserLocation);
		userInfoUserLocation.setCatalogsLocation(this);

		return userInfoUserLocation;
	}

	public UserInfoUserLocation removeUserInfoUserLocation(UserInfoUserLocation userInfoUserLocation) {
		getUserInfoUserLocations().remove(userInfoUserLocation);
		userInfoUserLocation.setCatalogsLocation(null);

		return userInfoUserLocation;
	}
*/
}