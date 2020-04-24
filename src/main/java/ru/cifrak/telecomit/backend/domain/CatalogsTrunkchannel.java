package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the catalogs_trunkchannel database table.
 * 
 */
@Entity
@Table(name="catalogs_trunkchannel")
@NamedQuery(name="CatalogsTrunkchannel.findAll", query="SELECT c FROM CatalogsTrunkchannel c")
public class CatalogsTrunkchannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_TRUNKCHANNEL_ID_GENERATOR", sequenceName="SEQ_CATALOGS_TRUNKCHANNEL")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_TRUNKCHANNEL_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date commissioning;

	private Integer completed;

	@Temporal(TemporalType.DATE)
	private Date decommissioning;

	@Column(length=4)
	private String state;

	//bi-directional many-to-one association to CatalogsContract
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contract_id")
	private CatalogsContract catalogsContract;

	//bi-directional many-to-one association to CatalogsGovernmentdevelopmentprogram
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="government_program_id")
	private CatalogsGovernmentDevelopmentProgram catalogsGovernmentdevelopmentprogram;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_end_id", nullable=false)
	private CatalogsLocation catalogsLocation1;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_start_id", nullable=false)
	private CatalogsLocation catalogsLocation2;

	//bi-directional many-to-one association to CatalogsOperator
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operator_id", nullable=false)
	private CatalogsOperator catalogsOperator;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="previous_id")
	private CatalogsTrunkchannel catalogsTrunkchannel;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@OneToMany(mappedBy="catalogsTrunkchannel")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels;

	//bi-directional many-to-one association to CatalogsTrunkchanneltype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id", nullable=false)
	private CatalogsTrunkChannelType catalogsTrunkchanneltype;

	public CatalogsTrunkchannel() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCommissioning() {
		return this.commissioning;
	}

	public void setCommissioning(Date commissioning) {
		this.commissioning = commissioning;
	}

	public Integer getCompleted() {
		return this.completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Date getDecommissioning() {
		return this.decommissioning;
	}

	public void setDecommissioning(Date decommissioning) {
		this.decommissioning = decommissioning;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public CatalogsContract getCatalogsContract() {
		return this.catalogsContract;
	}

	public void setCatalogsContract(CatalogsContract catalogsContract) {
		this.catalogsContract = catalogsContract;
	}

	public CatalogsGovernmentDevelopmentProgram getCatalogsGovernmentdevelopmentprogram() {
		return this.catalogsGovernmentdevelopmentprogram;
	}

	public void setCatalogsGovernmentdevelopmentprogram(CatalogsGovernmentDevelopmentProgram catalogsGovernmentdevelopmentprogram) {
		this.catalogsGovernmentdevelopmentprogram = catalogsGovernmentdevelopmentprogram;
	}

	public CatalogsLocation getCatalogsLocation1() {
		return this.catalogsLocation1;
	}

	public void setCatalogsLocation1(CatalogsLocation catalogsLocation1) {
		this.catalogsLocation1 = catalogsLocation1;
	}

	public CatalogsLocation getCatalogsLocation2() {
		return this.catalogsLocation2;
	}

	public void setCatalogsLocation2(CatalogsLocation catalogsLocation2) {
		this.catalogsLocation2 = catalogsLocation2;
	}

	public CatalogsOperator getCatalogsOperator() {
		return this.catalogsOperator;
	}

	public void setCatalogsOperator(CatalogsOperator catalogsOperator) {
		this.catalogsOperator = catalogsOperator;
	}

	public CatalogsTrunkchannel getCatalogsTrunkchannel() {
		return this.catalogsTrunkchannel;
	}

	public void setCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		this.catalogsTrunkchannel = catalogsTrunkchannel;
	}

	public List<CatalogsTrunkchannel> getCatalogsTrunkchannels() {
		return this.catalogsTrunkchannels;
	}

	public void setCatalogsTrunkchannels(List<CatalogsTrunkchannel> catalogsTrunkchannels) {
		this.catalogsTrunkchannels = catalogsTrunkchannels;
	}

	public CatalogsTrunkchannel addCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().add(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsTrunkchannel(this);

		return catalogsTrunkchannel;
	}

	public CatalogsTrunkchannel removeCatalogsTrunkchannel(CatalogsTrunkchannel catalogsTrunkchannel) {
		getCatalogsTrunkchannels().remove(catalogsTrunkchannel);
		catalogsTrunkchannel.setCatalogsTrunkchannel(null);

		return catalogsTrunkchannel;
	}

	public CatalogsTrunkChannelType getCatalogsTrunkchanneltype() {
		return this.catalogsTrunkchanneltype;
	}

	public void setCatalogsTrunkchanneltype(CatalogsTrunkChannelType catalogsTrunkchanneltype) {
		this.catalogsTrunkchanneltype = catalogsTrunkchanneltype;
	}

}