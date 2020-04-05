package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.cifrak.telecomit.backend.serializer.IconSerializer;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_operator database table.
 * 
 */
@Entity
@Table(name="catalogs_operator")
@NamedQuery(name="CatalogsOperator.findAll", query="SELECT c FROM CatalogsOperator c")
@Data
public class CatalogsOperator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_OPERATOR_ID_GENERATOR", sequenceName="catalogs_operator_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_OPERATOR_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String contacts;

	@Column(length=100)
	@JsonSerialize(using = IconSerializer.class)
	private String icon;

	@Column(length=12)
	private String inn;

	@JsonProperty("juristic_name")
	@Column(name="juristic_name", nullable=false, length=256)
	private String juristicName;

	@Column(length=9)
	private String kpp;

	@Column(nullable=false, length=256)
	private String name;

	@Column(nullable=false, length=40)
	private String services;

	@Column(length=5)
	private String type;
}