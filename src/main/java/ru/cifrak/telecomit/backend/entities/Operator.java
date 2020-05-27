package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor

@Entity
@Table
@NamedQuery(name="CatalogsOperator.findAll", query="SELECT c FROM Operator c")
public class Operator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OPERATOR_ID_GENERATOR", sequenceName="operator_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OPERATOR_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(columnDefinition = "text")
	private String contacts;

	@Column(length=100)
//	@JsonSerialize(using = IconSerializer.class)
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