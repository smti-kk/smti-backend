package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

	@Column(name="juristic_name", nullable=false, length=256)
	private String juristicName;

	@Column(length=9)
	private String kpp;

	@Column(nullable=false, length=256)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "operator_operator_service",
			joinColumns = {@JoinColumn(name = "operator_id")},
			inverseJoinColumns = {@JoinColumn(name = "services_id")})
	private List<OperatorService> services;

	@Column(length=5)
	private String type;

	public Operator(Integer id,
					String name,
					String contacts,
					String juristicName,
					List<OperatorService> services) {
		this.id = id;
		this.name = name;
		this.contacts = contacts;
		this.juristicName = juristicName;
		this.services = services;
	}
}