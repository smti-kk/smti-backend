package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the catalogs_governmentdevelopmentprogram database table.
 * 
 */
@Data
@Entity
@Table(name="catalogs_governmentdevelopmentprogram")
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


	public CatalogsGovernmentDevelopmentProgram() {
	}

	public CatalogsGovernmentDevelopmentProgram(Integer id, String description, String fullName, String shortName) {
		this.id = id;
		this.description = description;
		this.fullName = fullName;
		this.shortName = shortName;
	}
}