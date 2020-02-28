package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the catalogs_internetaccesstype database table.
 * 
 */
@Entity
@Table(name="catalogs_internetaccesstype")
@NamedQuery(name="CatalogsInternetaccesstype.findAll", query="SELECT c FROM CatalogsInternetaccesstype c")
public class CatalogsInternetaccesstype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_INTERNETACCESSTYPE_ID_GENERATOR", sequenceName="catalogs_internetaccesstype_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_INTERNETACCESSTYPE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=256)
	private String name;

	@JsonIgnore
	@Column(name="report_weight", nullable=false)
	private Integer reportWeight;

/*
	//bi-directional many-to-one association to CatalogsNetworkconnection
	@OneToMany(mappedBy="catalogsInternetaccesstype")
	private List<CatalogsNetworkconnection> catalogsNetworkconnections;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@OneToMany(mappedBy="catalogsInternetaccesstype")
	private List<MonitoringAccesspointRe> monitoringAccesspointRes;
*/

	public CatalogsInternetaccesstype() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getReportWeight() {
		return this.reportWeight;
	}

	public void setReportWeight(Integer reportWeight) {
		this.reportWeight = reportWeight;
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
		catalogsNetworkconnection.setCatalogsInternetaccesstype(this);

		return catalogsNetworkconnection;
	}

	public CatalogsNetworkconnection removeCatalogsNetworkconnection(CatalogsNetworkconnection catalogsNetworkconnection) {
		getCatalogsNetworkconnections().remove(catalogsNetworkconnection);
		catalogsNetworkconnection.setCatalogsInternetaccesstype(null);

		return catalogsNetworkconnection;
	}

	public List<MonitoringAccesspointRe> getMonitoringAccesspointRes() {
		return this.monitoringAccesspointRes;
	}

	public void setMonitoringAccesspointRes(List<MonitoringAccesspointRe> monitoringAccesspointRes) {
		this.monitoringAccesspointRes = monitoringAccesspointRes;
	}

	public MonitoringAccesspointRe addMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		getMonitoringAccesspointRes().add(monitoringAccesspointRe);
		monitoringAccesspointRe.setCatalogsInternetaccesstype(this);

		return monitoringAccesspointRe;
	}

	public MonitoringAccesspointRe removeMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		getMonitoringAccesspointRes().remove(monitoringAccesspointRe);
		monitoringAccesspointRe.setCatalogsInternetaccesstype(null);

		return monitoringAccesspointRe;
	}
*/

}