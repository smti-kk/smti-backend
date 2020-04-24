package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_organizationtype database table.
 * 
 */
@Entity
@Table(name="catalogs_organizationtype")
@NamedQuery(name="CatalogsOrganizationtype.findAll", query="SELECT c FROM CatalogsOrganizationtype c")
public class CatalogsOrganizationtype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_ORGANIZATIONTYPE_ID_GENERATOR", sequenceName="catalogs_organizationtype_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_ORGANIZATIONTYPE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=500)
	private String name;

	@JsonBackReference
	//bi-directional many-to-one association to CatalogsOrganization
	@OneToMany(mappedBy= "type")
	private List<CatalogsOrganization> catalogsOrganizations;

	@JsonIgnore
	//bi-directional many-to-one association to CatalogsOrganizationAvailableOrganizationType
	@OneToMany(mappedBy="catalogsOrganizationtype")
	private List<CatalogsOrganizationAvailableOrganizationType> catalogsOrganizationAvailableOrganizationTypes;

	public CatalogsOrganizationtype() {
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
		catalogsOrganization.setType(this);

		return catalogsOrganization;
	}

	public CatalogsOrganization removeCatalogsOrganization(CatalogsOrganization catalogsOrganization) {
		getCatalogsOrganizations().remove(catalogsOrganization);
		catalogsOrganization.setType(null);

		return catalogsOrganization;
	}

	public List<CatalogsOrganizationAvailableOrganizationType> getCatalogsOrganizationAvailableOrganizationTypes() {
		return this.catalogsOrganizationAvailableOrganizationTypes;
	}

	public void setCatalogsOrganizationAvailableOrganizationTypes(List<CatalogsOrganizationAvailableOrganizationType> catalogsOrganizationAvailableOrganizationTypes) {
		this.catalogsOrganizationAvailableOrganizationTypes = catalogsOrganizationAvailableOrganizationTypes;
	}

	public CatalogsOrganizationAvailableOrganizationType addCatalogsOrganizationAvailableOrganizationType(CatalogsOrganizationAvailableOrganizationType catalogsOrganizationAvailableOrganizationType) {
		getCatalogsOrganizationAvailableOrganizationTypes().add(catalogsOrganizationAvailableOrganizationType);
		catalogsOrganizationAvailableOrganizationType.setCatalogsOrganizationtype(this);

		return catalogsOrganizationAvailableOrganizationType;
	}

	public CatalogsOrganizationAvailableOrganizationType removeCatalogsOrganizationAvailableOrganizationType(CatalogsOrganizationAvailableOrganizationType catalogsOrganizationAvailableOrganizationType) {
		getCatalogsOrganizationAvailableOrganizationTypes().remove(catalogsOrganizationAvailableOrganizationType);
		catalogsOrganizationAvailableOrganizationType.setCatalogsOrganizationtype(null);

		return catalogsOrganizationAvailableOrganizationType;
	}

}