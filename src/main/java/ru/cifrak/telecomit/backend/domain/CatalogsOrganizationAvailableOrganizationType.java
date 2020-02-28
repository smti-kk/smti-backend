package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogs_organization_available_organization_types database table.
 * 
 */
@Entity
@Table(name="catalogs_organization_available_organization_types")
@NamedQuery(name="CatalogsOrganizationAvailableOrganizationType.findAll", query="SELECT c FROM CatalogsOrganizationAvailableOrganizationType c")
public class CatalogsOrganizationAvailableOrganizationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_ORGANIZATION_AVAILABLE_ORGANIZATION_TYPES_ID_GENERATOR", sequenceName="catalogs_organization_available_organization_types_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_ORGANIZATION_AVAILABLE_ORGANIZATION_TYPES_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to CatalogsOrganization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organization_id", nullable=false)
	private CatalogsOrganization catalogsOrganization;

	//bi-directional many-to-one association to CatalogsOrganizationtype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organizationtype_id", nullable=false)
	private CatalogsOrganizationtype catalogsOrganizationtype;

	public CatalogsOrganizationAvailableOrganizationType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CatalogsOrganization getCatalogsOrganization() {
		return this.catalogsOrganization;
	}

	public void setCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		this.catalogsOrganization = catalogsOrganization;
	}

	public CatalogsOrganizationtype getCatalogsOrganizationtype() {
		return this.catalogsOrganizationtype;
	}

	public void setCatalogsOrganizationtype(CatalogsOrganizationtype catalogsOrganizationtype) {
		this.catalogsOrganizationtype = catalogsOrganizationtype;
	}

}