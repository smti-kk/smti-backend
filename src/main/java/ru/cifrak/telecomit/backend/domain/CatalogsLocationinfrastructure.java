package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogs_locationinfrastructure database table.
 * 
 */
@Entity
@Table(name="catalogs_locationinfrastructure")
@NamedQuery(name="CatalogsLocationinfrastructure.findAll", query="SELECT c FROM CatalogsLocationinfrastructure c")
public class CatalogsLocationinfrastructure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_LOCATIONINFRASTRUCTURE_ID_GENERATOR", sequenceName="catalogs_locationinfrastructure_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_LOCATIONINFRASTRUCTURE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="project_down_analog", nullable=false)
	private Boolean projectDownAnalog;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id", nullable=false)
	private CatalogsLocation catalogsLocation;

	public CatalogsLocationinfrastructure() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getProjectDownAnalog() {
		return this.projectDownAnalog;
	}

	public void setProjectDownAnalog(Boolean projectDownAnalog) {
		this.projectDownAnalog = projectDownAnalog;
	}

	public CatalogsLocation getCatalogsLocation() {
		return this.catalogsLocation;
	}

	public void setCatalogsLocation(CatalogsLocation catalogsLocation) {
		this.catalogsLocation = catalogsLocation;
	}

}