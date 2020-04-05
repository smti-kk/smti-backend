package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the catalogs_organization database table.
 * 
 */
@Data
@Entity
@Table(name="catalogs_organization")
@NamedQuery(name="CatalogsOrganization.findAll", query="SELECT c FROM CatalogsOrganization c")
public class CatalogsOrganization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_ORGANIZATION_ID_GENERATOR", sequenceName="catalogs_organization_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_ORGANIZATION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=2147483647)
	private String address;

	@Column(nullable=false)
	private UUID fias;

	@JsonProperty("full_name")
	@Column(name="full_name", nullable=false, length=2147483647)
	private String fullName;

	@Column(nullable=false)
	private Integer inn;

	@Column(nullable=false)
	private Integer kpp;

/*
	@Column(nullable=false, length=500)
	private String name;
	//bi-directional many-to-one association to CatalogsContract
	@OneToMany(mappedBy="catalogsOrganization")
	private List<CatalogsContract> catalogsContracts;
*/

/*
	//bi-directional many-to-one association to CatalogsNetworkconnection
	@OneToMany(mappedBy="catalogsOrganization")
	private List<CatalogsNetworkconnection> catalogsNetworkconnections;
*/

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id", nullable=false)
	private CatalogsLocation catalogsLocation;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsOrganization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private CatalogsOrganization catalogsOrganization;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsOrganization
	@OneToMany(mappedBy="catalogsOrganization")
	private List<CatalogsOrganization> catalogsOrganizations;

	@JsonManagedReference
	//bi-directional many-to-one association to CatalogsOrganizationtype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	@JsonProperty("type")
	private CatalogsOrganizationtype catalogsOrganizationtype;

	@JsonManagedReference
	//bi-directional many-to-one association to CatalogsSmotype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_smo_id")
	@JsonProperty("type_smo")
	private CatalogsSmotype catalogsSmotype;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsOrganizationAvailableOrganizationType
	@OneToMany(mappedBy="catalogsOrganization")
	private List<CatalogsOrganizationAvailableOrganizationType> catalogsOrganizationAvailableOrganizationTypes;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsOrganizationAvailableSmoType
	@OneToMany(mappedBy="catalogsOrganization")
	private List<CatalogsOrganizationAvailableSmoType> catalogsOrganizationAvailableSmoTypes;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@JsonProperty("reaccesspoints")
	@JsonManagedReference
	@OneToMany(mappedBy="catalogsOrganization")
	private List<MonitoringAccesspointRe> monitoringAccesspointRes;

}