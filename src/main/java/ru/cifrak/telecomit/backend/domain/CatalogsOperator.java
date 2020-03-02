package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_operator database table.
 * 
 */
@Entity
@Table(name="catalogs_operator")
@NamedQuery(name="CatalogsOperator.findAll", query="SELECT c FROM CatalogsOperator c")
public class CatalogsOperator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_OPERATOR_ID_GENERATOR", sequenceName="catalogs_operator_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_OPERATOR_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String contacts;

	@Column(length=100)
	private String icon;

	@Column(length=12)
	private String inn;

	@JsonProperty("juristic_name")
	@Column(name="juristic_name", nullable=false, length=256)
	private String juristicName;

	@Column(length=9)
	private String kpp;

	@Column(nullable=false, length=256)
	private String name;

	@Column(nullable=false, length=40)
	private String services;

	@Column(length=5)
	private String type;

/*	//bi-directional many-to-one association to CatalogsNetworkconnection
	@OneToMany(mappedBy="catalogsOperator")
	private List<CatalogsNetworkconnection> catalogsNetworkconnections;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@OneToMany(mappedBy="catalogsOperator")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels;

	//bi-directional many-to-one association to FtcAts
	@OneToMany(mappedBy="catalogsOperator")
	private List<FtcAts> ftcAts;

	//bi-directional many-to-one association to FtcInternet
	@OneToMany(mappedBy="catalogsOperator")
	private List<FtcInternet> ftcInternets;

	//bi-directional many-to-one association to FtcMobile
	@OneToMany(mappedBy="catalogsOperator")
	private List<FtcMobile> ftcMobiles;

	//bi-directional many-to-one association to FtcPost
	@OneToMany(mappedBy="catalogsOperator")
	private List<FtcPost> ftcPosts;

	//bi-directional many-to-one association to FtcRadio
	@OneToMany(mappedBy="catalogsOperator")
	private List<FtcRadio> ftcRadios;

	//bi-directional many-to-one association to FtcTelevision
	@OneToMany(mappedBy="catalogsOperator")
	private List<FtcTelevision> ftcTelevisions;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@OneToMany(mappedBy="catalogsOperator")
	private List<MonitoringAccesspointRe> monitoringAccesspointRes;

	//bi-directional many-to-one association to UserInfoUser
	@OneToMany(mappedBy="catalogsOperator")
	private List<UserInfoUser> userInfoUsers;*/

	public CatalogsOperator() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContacts() {
		return this.contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getIcon() {
		return "/media/" + this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getInn() {
		return this.inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getJuristicName() {
		return this.juristicName;
	}

	public void setJuristicName(String juristicName) {
		this.juristicName = juristicName;
	}

	public String getKpp() {
		return this.kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getServices() {
		return this.services.split(",");
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
		catalogsNetworkconnection.setCatalogsOperator(this);

		return catalogsNetworkconnection;
	}

	public CatalogsNetworkconnection removeCatalogsNetworkconnection(CatalogsNetworkconnection catalogsNetworkconnection) {
		getCatalogsNetworkconnections().remove(catalogsNetworkconnection);
		catalogsNetworkconnection.setCatalogsOperator(null);

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
		catalogsTrunkchannel.setCatalogsOperator(this);

		return catalogsTrunkchannel;
	}

	public CatalogsTrunkchannel removeCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().remove(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsOperator(null);

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
		ftcAts.setCatalogsOperator(this);

		return ftcAts;
	}

	public FtcAts removeFtcAts(FtcAts ftcAts) {
		getFtcAts().remove(ftcAts);
		ftcAts.setCatalogsOperator(null);

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
		ftcInternet.setCatalogsOperator(this);

		return ftcInternet;
	}

	public FtcInternet removeFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().remove(ftcInternet);
		ftcInternet.setCatalogsOperator(null);

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
		ftcMobile.setCatalogsOperator(this);

		return ftcMobile;
	}

	public FtcMobile removeFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().remove(ftcMobile);
		ftcMobile.setCatalogsOperator(null);

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
		ftcPost.setCatalogsOperator(this);

		return ftcPost;
	}

	public FtcPost removeFtcPost(FtcPost ftcPost) {
		getFtcPosts().remove(ftcPost);
		ftcPost.setCatalogsOperator(null);

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
		ftcRadio.setCatalogsOperator(this);

		return ftcRadio;
	}

	public FtcRadio removeFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().remove(ftcRadio);
		ftcRadio.setCatalogsOperator(null);

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
		ftcTelevision.setCatalogsOperator(this);

		return ftcTelevision;
	}

	public FtcTelevision removeFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().remove(ftcTelevision);
		ftcTelevision.setCatalogsOperator(null);

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
		monitoringAccesspointRe.setCatalogsOperator(this);

		return monitoringAccesspointRe;
	}

	public MonitoringAccesspointRe removeMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		getMonitoringAccesspointRes().remove(monitoringAccesspointRe);
		monitoringAccesspointRe.setCatalogsOperator(null);

		return monitoringAccesspointRe;
	}

	public List<UserInfoUser> getUserInfoUsers() {
		return this.userInfoUsers;
	}

	public void setUserInfoUsers(List<UserInfoUser> userInfoUsers) {
		this.userInfoUsers = userInfoUsers;
	}

	public UserInfoUser addUserInfoUser(UserInfoUser userInfoUser) {
		getUserInfoUsers().add(userInfoUser);
		userInfoUser.setCatalogsOperator(this);

		return userInfoUser;
	}

	public UserInfoUser removeUserInfoUser(UserInfoUser userInfoUser) {
		getUserInfoUsers().remove(userInfoUser);
		userInfoUser.setCatalogsOperator(null);

		return userInfoUser;
	}
*/
}