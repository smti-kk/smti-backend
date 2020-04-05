package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_communicationservicesaddcause database table.
 * 
 */
@Entity
@Table(name="catalogs_communicationservicesaddcause")
@NamedQuery(name="CatalogsCommunicationservicesaddcause.findAll", query="SELECT c FROM CatalogsCommunicationservicesaddcause c")
public class CatalogsCommunicationservicesaddcause implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_COMMUNICATIONSERVICESADDCAUSE_ID_GENERATOR", sequenceName="SEQ_CATALOGS_COMMUNICATIONSERVICESADDCAUSE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_COMMUNICATIONSERVICESADDCAUSE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=256)
	private String name;

	@Column(name="type_cause", nullable=false)
	private Integer typeCause;

	//bi-directional many-to-one association to CatalogsInfomat
	@OneToMany(mappedBy="catalogsCommunicationservicesaddcause")
	private List<CatalogsInfomat> catalogsInfomats;

	public CatalogsCommunicationservicesaddcause() {
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

	public Integer getTypeCause() {
		return this.typeCause;
	}

	public void setTypeCause(Integer typeCause) {
		this.typeCause = typeCause;
	}

	public List<CatalogsInfomat> getCatalogsInfomats() {
		return this.catalogsInfomats;
	}

	public void setCatalogsInfomats(List<CatalogsInfomat> catalogsInfomats) {
		this.catalogsInfomats = catalogsInfomats;
	}

	public CatalogsInfomat addCatalogsInfomat(CatalogsInfomat catalogsInfomat) {
		getCatalogsInfomats().add(catalogsInfomat);
		catalogsInfomat.setCatalogsCommunicationservicesaddcause(this);

		return catalogsInfomat;
	}

	public CatalogsInfomat removeCatalogsInfomat(CatalogsInfomat catalogsInfomat) {
		getCatalogsInfomats().remove(catalogsInfomat);
		catalogsInfomat.setCatalogsCommunicationservicesaddcause(null);

		return catalogsInfomat;
	}

}