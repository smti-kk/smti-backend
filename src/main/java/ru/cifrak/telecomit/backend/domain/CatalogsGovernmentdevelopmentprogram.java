package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_governmentdevelopmentprogram database table.
 * 
 */
@Entity
@Table(name="catalogs_governmentdevelopmentprogram")
@NamedQuery(name="CatalogsGovernmentdevelopmentprogram.findAll", query="SELECT c FROM CatalogsGovernmentdevelopmentprogram c")
public class CatalogsGovernmentdevelopmentprogram implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_GOVERNMENTDEVELOPMENTPROGRAM_ID_GENERATOR", sequenceName="catalogs_governmentdevelopmentprogram_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_GOVERNMENTDEVELOPMENTPROGRAM_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String description;

	@JsonProperty("full_name")
	@Column(name="full_name", nullable=false, length=1000)
	private String fullName;

	@JsonProperty("short_name")
	@Column(name="short_name", nullable=false, length=100)
	private String shortName;

/*
	//bi-directional many-to-one association to CatalogsNetworkconnection
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<CatalogsNetworkconnection> catalogsNetworkconnections;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels;

	//bi-directional many-to-one association to FtcAts
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<FtcAts> ftcAts;

	//bi-directional many-to-one association to FtcInternet
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<FtcInternet> ftcInternets;

	//bi-directional many-to-one association to FtcMobile
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<FtcMobile> ftcMobiles;

	//bi-directional many-to-one association to FtcPost
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<FtcPost> ftcPosts;

	//bi-directional many-to-one association to FtcRadio
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<FtcRadio> ftcRadios;

	//bi-directional many-to-one association to FtcTelevision
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<FtcTelevision> ftcTelevisions;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<MonitoringAccesspointRe> monitoringAccesspointRes;
*/

	public CatalogsGovernmentdevelopmentprogram() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
/*

	public List<CatalogsNetworkconnection> getCatalogsNetworkconnections() {
		return this.catalogsNetworkconnections;
	}

	public void setCatalogsNetworkconnections(List<CatalogsNetworkconnection> catalogsNetworkconnections) {
		this.catalogsNetworkconnections = catalogsNetworkconnections;
	}

	public CatalogsNetworkconnection addCatalogsNetworkconnection(CatalogsNetworkconnection catalogsNetworkconnection) {
		getCatalogsNetworkconnections().add(catalogsNetworkconnection);
		catalogsNetworkconnection.setCatalogsGovernmentdevelopmentprogram(this);

		return catalogsNetworkconnection;
	}

	public CatalogsNetworkconnection removeCatalogsNetworkconnection(CatalogsNetworkconnection catalogsNetworkconnection) {
		getCatalogsNetworkconnections().remove(catalogsNetworkconnection);
		catalogsNetworkconnection.setCatalogsGovernmentdevelopmentprogram(null);

		return catalogsNetworkconnection;
	}

	public List<CatalogsTrunkchannel> getCatalogsTrunkchannels() {
		return this.catalogsTrunkchannels;
	}

	public void setCatalogsTrunkchannels(List<CatalogsTrunkchannel> catalogsTrunkchannels) {
		this.catalogsTrunkchannels = catalogsTrunkchannels;
	}

	public CatalogsTrunkchannel addCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().add(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsGovernmentdevelopmentprogram(this);

		return catalogsTrunkchannel;
	}

	public CatalogsTrunkchannel removeCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().remove(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsGovernmentdevelopmentprogram(null);

		return catalogsTrunkchannel;
	}

	public List<FtcAts> getFtcAts() {
		return this.ftcAts;
	}

	public void setFtcAts(List<FtcAts> ftcAts) {
		this.ftcAts = ftcAts;
	}

	public FtcAts addFtcAts(FtcAts ftcAts) {
		getFtcAts().add(ftcAts);
		ftcAts.setCatalogsGovernmentdevelopmentprogram(this);

		return ftcAts;
	}

	public FtcAts removeFtcAts(FtcAts ftcAts) {
		getFtcAts().remove(ftcAts);
		ftcAts.setCatalogsGovernmentdevelopmentprogram(null);

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
		ftcInternet.setCatalogsGovernmentdevelopmentprogram(this);

		return ftcInternet;
	}

	public FtcInternet removeFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().remove(ftcInternet);
		ftcInternet.setCatalogsGovernmentdevelopmentprogram(null);

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
		ftcMobile.setCatalogsGovernmentdevelopmentprogram(this);

		return ftcMobile;
	}

	public FtcMobile removeFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().remove(ftcMobile);
		ftcMobile.setCatalogsGovernmentdevelopmentprogram(null);

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
		ftcPost.setCatalogsGovernmentdevelopmentprogram(this);

		return ftcPost;
	}

	public FtcPost removeFtcPost(FtcPost ftcPost) {
		getFtcPosts().remove(ftcPost);
		ftcPost.setCatalogsGovernmentdevelopmentprogram(null);

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
		ftcRadio.setCatalogsGovernmentdevelopmentprogram(this);

		return ftcRadio;
	}

	public FtcRadio removeFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().remove(ftcRadio);
		ftcRadio.setCatalogsGovernmentdevelopmentprogram(null);

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
		ftcTelevision.setCatalogsGovernmentdevelopmentprogram(this);

		return ftcTelevision;
	}

	public FtcTelevision removeFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().remove(ftcTelevision);
		ftcTelevision.setCatalogsGovernmentdevelopmentprogram(null);

		return ftcTelevision;
	}

	public List<MonitoringAccesspointRe> getMonitoringAccesspointRes() {
		return this.monitoringAccesspointRes;
	}

	public void setMonitoringAccesspointRes(List<MonitoringAccesspointRe> monitoringAccesspointRes) {
		this.monitoringAccesspointRes = monitoringAccesspointRes;
	}

	public MonitoringAccesspointRe addMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		getMonitoringAccesspointRes().add(monitoringAccesspointRe);
		monitoringAccesspointRe.setCatalogsGovernmentdevelopmentprogram(this);

		return monitoringAccesspointRe;
	}

	public MonitoringAccesspointRe removeMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		getMonitoringAccesspointRes().remove(monitoringAccesspointRe);
		monitoringAccesspointRe.setCatalogsGovernmentdevelopmentprogram(null);

		return monitoringAccesspointRe;
	}
*/

}