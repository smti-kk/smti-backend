package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_mobiletype database table.
 * 
 */
@Entity
@Table(name="catalogs_mobiletype")
@NamedQuery(name="CatalogsMobiletype.findAll", query="SELECT c FROM CatalogsMobiletype c")
public class CatalogsMobiletype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_MOBILETYPE_ID_GENERATOR", sequenceName="catalogs_mobiletype_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_MOBILETYPE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=256)
	private String name;

/*
	//bi-directional many-to-one association to FtcMobile
	@OneToMany(mappedBy="catalogsMobiletype")
	private List<FtcMobile> ftcMobiles;
*/

	public CatalogsMobiletype() {
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
/*

	public List<FtcMobile> getFtcMobiles() {
		return this.ftcMobiles;
	}

	public void setFtcMobiles(List<FtcMobile> ftcMobiles) {
		this.ftcMobiles = ftcMobiles;
	}

	public FtcMobile addFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().add(ftcMobile);
		ftcMobile.setCatalogsMobiletype(this);

		return ftcMobile;
	}

	public FtcMobile removeFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().remove(ftcMobile);
		ftcMobile.setCatalogsMobiletype(null);

		return ftcMobile;
	}
*/

}