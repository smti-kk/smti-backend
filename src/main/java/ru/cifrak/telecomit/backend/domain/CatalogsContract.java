package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the catalogs_contract database table.
 * 
 */
@Entity
@Table(name="catalogs_contract")
@NamedQuery(name="CatalogsContract.findAll", query="SELECT c FROM CatalogsContract c")
public class CatalogsContract implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_CONTRACT_ID_GENERATOR", sequenceName="SEQ_CATALOGS_CONTRACT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_CONTRACT_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, precision=15, scale=2)
	private BigDecimal amount;

	@Temporal(TemporalType.DATE)
	@Column(name="date_realization", nullable=false)
	private Date dateRealization;

	@Column(name="registration_number", nullable=false, length=100)
	private String registrationNumber;

	//bi-directional many-to-one association to CatalogsOrganization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id", nullable=false)
	private CatalogsOrganization catalogsOrganization;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@OneToMany(mappedBy="catalogsContract")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels;

	//bi-directional many-to-one association to FtcAts
	@OneToMany(mappedBy="catalogsContract")
	private List<FtcAts> ftcAts;

	//bi-directional many-to-one association to FtcInternet
	@OneToMany(mappedBy="catalogsContract")
	private List<FtcInternet> ftcInternets;

	//bi-directional many-to-one association to FtcMobile
	@OneToMany(mappedBy="catalogsContract")
	private List<FtcMobile> ftcMobiles;

	//bi-directional many-to-one association to FtcPost
	@OneToMany(mappedBy="catalogsContract")
	private List<FtcPost> ftcPosts;

	//bi-directional many-to-one association to FtcRadio
	@OneToMany(mappedBy="catalogsContract")
	private List<FtcRadio> ftcRadios;

	//bi-directional many-to-one association to FtcTelevision
	@OneToMany(mappedBy="catalogsContract")
	private List<FtcTelevision> ftcTelevisions;

	//bi-directional many-to-one association to MonitoringAccesspointRe
//	@OneToMany(mappedBy="catalogsContract")
//	private List<MonitoringAccesspointRe> monitoringAccesspointRes;

	public CatalogsContract() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDateRealization() {
		return this.dateRealization;
	}

	public void setDateRealization(Date dateRealization) {
		this.dateRealization = dateRealization;
	}

	public String getRegistrationNumber() {
		return this.registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public CatalogsOrganization getCatalogsOrganization() {
		return this.catalogsOrganization;
	}

	public void setCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		this.catalogsOrganization = catalogsOrganization;
	}

	public List<CatalogsTrunkchannel> getCatalogsTrunkchannels() {
		return this.catalogsTrunkchannels;
	}

	public void setCatalogsTrunkchannels(List<CatalogsTrunkchannel> catalogsTrunkchannels) {
		this.catalogsTrunkchannels = catalogsTrunkchannels;
	}

	public CatalogsTrunkchannel addCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().add(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsContract(this);

		return catalogsTrunkchannel;
	}

	public CatalogsTrunkchannel removeCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().remove(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsContract(null);

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
		ftcAts.setCatalogsContract(this);

		return ftcAts;
	}

	public FtcAts removeFtcAts(FtcAts ftcAts) {
		getFtcAts().remove(ftcAts);
		ftcAts.setCatalogsContract(null);

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
		ftcInternet.setCatalogsContract(this);

		return ftcInternet;
	}

	public FtcInternet removeFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().remove(ftcInternet);
		ftcInternet.setCatalogsContract(null);

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
		ftcMobile.setCatalogsContract(this);

		return ftcMobile;
	}

	public FtcMobile removeFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().remove(ftcMobile);
		ftcMobile.setCatalogsContract(null);

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
		ftcPost.setCatalogsContract(this);

		return ftcPost;
	}

	public FtcPost removeFtcPost(FtcPost ftcPost) {
		getFtcPosts().remove(ftcPost);
		ftcPost.setCatalogsContract(null);

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
		ftcRadio.setCatalogsContract(this);

		return ftcRadio;
	}

	public FtcRadio removeFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().remove(ftcRadio);
		ftcRadio.setCatalogsContract(null);

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
		ftcTelevision.setCatalogsContract(this);

		return ftcTelevision;
	}

	public FtcTelevision removeFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().remove(ftcTelevision);
		ftcTelevision.setCatalogsContract(null);

		return ftcTelevision;
	}

//	public List<MonitoringAccesspointRe> getMonitoringAccesspointRes() {
//		return this.monitoringAccesspointRes;
//	}
//
//	public void setMonitoringAccesspointRes(List<MonitoringAccesspointRe> monitoringAccesspointRes) {
//		this.monitoringAccesspointRes = monitoringAccesspointRes;
//	}

//	public MonitoringAccesspointRe addMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
//		getMonitoringAccesspointRes().add(monitoringAccesspointRe);
//		monitoringAccesspointRe.setCatalogsContract(this);
//
//		return monitoringAccesspointRe;
//	}
//
//	public MonitoringAccesspointRe removeMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
//		getMonitoringAccesspointRes().remove(monitoringAccesspointRe);
//		monitoringAccesspointRe.setCatalogsContract(null);
//
//		return monitoringAccesspointRe;
//	}

}