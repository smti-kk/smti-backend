package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogs_networkconnection database table.
 * 
 */
@Entity
@Table(name="catalogs_networkconnection")
@NamedQuery(name="CatalogsNetworkconnection.findAll", query="SELECT c FROM CatalogsNetworkСonnection c")
public class CatalogsNetworkСonnection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_NETWORKCONNECTION_ID_GENERATOR", sequenceName="SEQ_CATALOGS_NETWORKCONNECTION")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_NETWORKCONNECTION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	private Integer completed;

	@Column(name="max_amount", nullable=false)
	private Integer maxAmount;

	@Column(nullable=false, length=6)
	private String quality;

	@Column(length=4)
	private String state;

	@Column(nullable=false)
	private double throughput;

	//bi-directional many-to-one association to CatalogsGovernmentdevelopmentprogram
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="government_program_id")
	private CatalogsGovernmentDevelopmentProgram catalogsGovernmentdevelopmentprogram;

	//bi-directional many-to-one association to CatalogsInternetaccesstype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_internet_id", nullable=false)
	private CatalogsInternetaccesstype catalogsInternetaccesstype;

	//bi-directional many-to-one association to CatalogsOperator
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operator_id", nullable=false)
	private CatalogsOperator catalogsOperator;

	//bi-directional many-to-one association to CatalogsOrganization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organization_id", nullable=false)
	private CatalogsOrganization catalogsOrganization;

	//bi-directional many-to-one association to CatalogsTrunkchanneltype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_trunkchannel_id", nullable=false)
	private CatalogsTrunkChannelType catalogsTrunkchanneltype;

	public CatalogsNetworkСonnection() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompleted() {
		return this.completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Integer getMaxAmount() {
		return this.maxAmount;
	}

	public void setMaxAmount(Integer maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getThroughput() {
		return this.throughput;
	}

	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

	public CatalogsGovernmentDevelopmentProgram getCatalogsGovernmentdevelopmentprogram() {
		return this.catalogsGovernmentdevelopmentprogram;
	}

	public void setCatalogsGovernmentdevelopmentprogram(CatalogsGovernmentDevelopmentProgram catalogsGovernmentdevelopmentprogram) {
		this.catalogsGovernmentdevelopmentprogram = catalogsGovernmentdevelopmentprogram;
	}

	public CatalogsInternetaccesstype getCatalogsInternetaccesstype() {
		return this.catalogsInternetaccesstype;
	}

	public void setCatalogsInternetaccesstype(CatalogsInternetaccesstype catalogsInternetaccesstype) {
		this.catalogsInternetaccesstype = catalogsInternetaccesstype;
	}

	public CatalogsOperator getCatalogsOperator() {
		return this.catalogsOperator;
	}

	public void setCatalogsOperator(CatalogsOperator catalogsOperator) {
		this.catalogsOperator = catalogsOperator;
	}

	public CatalogsOrganization getCatalogsOrganization() {
		return this.catalogsOrganization;
	}

	public void setCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		this.catalogsOrganization = catalogsOrganization;
	}

	public CatalogsTrunkChannelType getCatalogsTrunkchanneltype() {
		return this.catalogsTrunkchanneltype;
	}

	public void setCatalogsTrunkchanneltype(CatalogsTrunkChannelType catalogsTrunkchanneltype) {
		this.catalogsTrunkchanneltype = catalogsTrunkchanneltype;
	}

}