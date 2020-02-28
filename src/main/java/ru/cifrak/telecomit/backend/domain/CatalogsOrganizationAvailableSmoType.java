package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogs_organization_available_smo_types database table.
 * 
 */
@Entity
@Table(name="catalogs_organization_available_smo_types")
@NamedQuery(name="CatalogsOrganizationAvailableSmoType.findAll", query="SELECT c FROM CatalogsOrganizationAvailableSmoType c")
public class CatalogsOrganizationAvailableSmoType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_ORGANIZATION_AVAILABLE_SMO_TYPES_ID_GENERATOR", sequenceName="catalogs_organization_available_smo_types_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_ORGANIZATION_AVAILABLE_SMO_TYPES_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to CatalogsOrganization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organization_id", nullable=false)
	private CatalogsOrganization catalogsOrganization;

	//bi-directional many-to-one association to CatalogsSmotype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="smotype_id", nullable=false)
	private CatalogsSmotype catalogsSmotype;

	public CatalogsOrganizationAvailableSmoType() {
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

	public CatalogsSmotype getCatalogsSmotype() {
		return this.catalogsSmotype;
	}

	public void setCatalogsSmotype(CatalogsSmotype catalogsSmotype) {
		this.catalogsSmotype = catalogsSmotype;
	}

}