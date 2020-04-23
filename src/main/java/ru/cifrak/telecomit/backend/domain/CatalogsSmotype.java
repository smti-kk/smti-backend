package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_smotype database table.
 * 
 */
@Entity
@Table(name="catalogs_smotype")
@NamedQuery(name="CatalogsSmotype.findAll", query="SELECT c FROM CatalogsSmotype c")
public class CatalogsSmotype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_SMOTYPE_ID_GENERATOR", sequenceName="catalogs_smotype_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_SMOTYPE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=500)
	private String name;

	@JsonBackReference
	//bi-directional many-to-one association to CatalogsOrganization
	@OneToMany(mappedBy= "smoType")
	private List<CatalogsOrganization> catalogsOrganizations;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsOrganizationAvailableSmoType
	@OneToMany(mappedBy="catalogsSmotype")
	private List<CatalogsOrganizationAvailableSmoType> catalogsOrganizationAvailableSmoTypes;

	public CatalogsSmotype() {
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

	public List<CatalogsOrganization> getCatalogsOrganizations() {
		return this.catalogsOrganizations;
	}

	public void setCatalogsOrganizations(List<CatalogsOrganization> catalogsOrganizations) {
		this.catalogsOrganizations = catalogsOrganizations;
	}

	public CatalogsOrganization addCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		getCatalogsOrganizations().add(catalogsOrganization);
		catalogsOrganization.setSmoType(this);

		return catalogsOrganization;
	}

	public CatalogsOrganization removeCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		getCatalogsOrganizations().remove(catalogsOrganization);
		catalogsOrganization.setSmoType(null);

		return catalogsOrganization;
	}

	public List<CatalogsOrganizationAvailableSmoType> getCatalogsOrganizationAvailableSmoTypes() {
		return this.catalogsOrganizationAvailableSmoTypes;
	}

	public void setCatalogsOrganizationAvailableSmoTypes(List<CatalogsOrganizationAvailableSmoType> catalogsOrganizationAvailableSmoTypes) {
		this.catalogsOrganizationAvailableSmoTypes = catalogsOrganizationAvailableSmoTypes;
	}

	public CatalogsOrganizationAvailableSmoType addCatalogsOrganizationAvailableSmoType(CatalogsOrganizationAvailableSmoType catalogsOrganizationAvailableSmoType) {
		getCatalogsOrganizationAvailableSmoTypes().add(catalogsOrganizationAvailableSmoType);
		catalogsOrganizationAvailableSmoType.setCatalogsSmotype(this);

		return catalogsOrganizationAvailableSmoType;
	}

	public CatalogsOrganizationAvailableSmoType removeCatalogsOrganizationAvailableSmoType(CatalogsOrganizationAvailableSmoType catalogsOrganizationAvailableSmoType) {
		getCatalogsOrganizationAvailableSmoTypes().remove(catalogsOrganizationAvailableSmoType);
		catalogsOrganizationAvailableSmoType.setCatalogsSmotype(null);

		return catalogsOrganizationAvailableSmoType;
	}

}