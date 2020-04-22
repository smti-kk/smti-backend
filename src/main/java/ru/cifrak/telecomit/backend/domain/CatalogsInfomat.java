package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the catalogs_infomat database table.
 * 
 */
@Entity
@Table(name="catalogs_infomat")
@NamedQuery(name="CatalogsInfomat.findAll", query="SELECT c FROM CatalogsInfomat c")
public class CatalogsInfomat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_INFOMAT_ID_GENERATOR", sequenceName="SEQ_CATALOGS_INFOMAT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_INFOMAT_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String address;

	@Column(length=2147483647)
	private String contacts;

	@Temporal(TemporalType.DATE)
	@Column(name="install_date", nullable=false)
	private Date installDate;

	@Column(length=256)
	private String organization;

	@Column(nullable=false)
	private Boolean removed;

	@Column(name="serial_number", length=16)
	private String serialNumber;

	@Column(nullable=false)
	private double speed;

	//bi-directional many-to-one association to CatalogsCommunicationservicesaddcause
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="add_cause_id")
	private CatalogsCommunicationservicesaddcause catalogsCommunicationservicesaddcause;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id", nullable=false)
	@JsonBackReference
	private CatalogsLocation catalogsLocation;

	//bi-directional many-to-one association to CatalogsTrunkchanneltype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="trunk_channel_type_id")
	private CatalogsTrunkChannelType catalogsTrunkchanneltype;

	public CatalogsInfomat() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContacts() {
		return this.contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Date getInstallDate() {
		return this.installDate;
	}

	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public Boolean getRemoved() {
		return this.removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public CatalogsCommunicationservicesaddcause getCatalogsCommunicationservicesaddcause() {
		return this.catalogsCommunicationservicesaddcause;
	}

	public void setCatalogsCommunicationservicesaddcause(CatalogsCommunicationservicesaddcause catalogsCommunicationservicesaddcause) {
		this.catalogsCommunicationservicesaddcause = catalogsCommunicationservicesaddcause;
	}

	public CatalogsLocation getCatalogsLocation() {
		return this.catalogsLocation;
	}

	public void setCatalogsLocation(CatalogsLocation catalogsLocation) {
		this.catalogsLocation = catalogsLocation;
	}

	public CatalogsTrunkChannelType getCatalogsTrunkchanneltype() {
		return this.catalogsTrunkchanneltype;
	}

	public void setCatalogsTrunkchanneltype(CatalogsTrunkChannelType catalogsTrunkchanneltype) {
		this.catalogsTrunkchanneltype = catalogsTrunkchanneltype;
	}

}