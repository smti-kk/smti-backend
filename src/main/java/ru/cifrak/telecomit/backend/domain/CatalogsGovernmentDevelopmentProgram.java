package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_governmentdevelopmentprogram database table.
 * 
 */
@Data
@Entity
@Table(name="catalogs_governmentdevelopmentprogram")
@NamedQuery(name="CatalogsGovernmentdevelopmentprogram.findAll", query="SELECT c FROM CatalogsGovernmentDevelopmentProgram c")
public class CatalogsGovernmentDevelopmentProgram implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_GOVERNMENTDEVELOPMENTPROGRAM_ID_GENERATOR", sequenceName="catalogs_governmentdevelopmentprogram_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_GOVERNMENTDEVELOPMENTPROGRAM_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String description;

	@JsonProperty("full_name")
	@Column(name="full_name", nullable=false, length=1000)
	private String fullName;

	@JsonProperty("short_name")
	@Column(name="short_name", nullable=false, length=100)
	private String shortName;


	//bi-directional many-to-one association to CatalogsNetworkconnection
//	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
//	private List<CatalogsNetworkconnection> catalogsNetworkconnections;

	//bi-directional many-to-one association to CatalogsTrunkchannel
	@JsonIgnore
	@OneToMany(mappedBy="catalogsGovernmentdevelopmentprogram")
	private List<CatalogsTrunkchannel> catalogsTrunkchannels;

	//bi-directional many-to-one association to FtcAts
	@JsonIgnore
	@OneToMany(mappedBy= "governmentProgram")
	private List<FtcAts> ftcAts;

	//bi-directional many-to-one association to FtcInternet
	@JsonIgnore
	@OneToMany(mappedBy= "governmentProgram")
	private List<FtcInternet> ftcInternets;

	//bi-directional many-to-one association to FtcMobile
	@JsonIgnore
	@OneToMany(mappedBy= "governmentProgram")
	private List<FtcMobile> ftcMobiles;

	//bi-directional many-to-one association to FtcPost
	@JsonIgnore
	@OneToMany(mappedBy= "governmentProgram")
	private List<FtcPost> ftcPosts;

	//bi-directional many-to-one association to FtcRadio
	@JsonIgnore
	@OneToMany(mappedBy= "governmentProgram")
	private List<FtcRadio> ftcRadios;

	//bi-directional many-to-one association to FtcTelevision
	@JsonIgnore
	@OneToMany(mappedBy= "governmentProgram")
	private List<FtcTelevision> ftcTelevisions;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@JsonIgnore
	@OneToMany(mappedBy="catalogsGovernmentDevelopmentProgram")
	private List<MonitoringAccesspointRe> monitoringAccesspointRes;


	public CatalogsGovernmentDevelopmentProgram() {
	}

	public CatalogsGovernmentDevelopmentProgram(Integer id, String description, String fullName, String shortName) {
		this.id = id;
		this.description = description;
		this.fullName = fullName;
		this.shortName = shortName;
	}
}