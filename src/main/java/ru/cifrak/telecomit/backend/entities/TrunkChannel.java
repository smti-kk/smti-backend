package ru.cifrak.telecomit.backend.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;



@Entity
@Table
@NamedQuery(name="Trunkchannel.findAll", query="SELECT c FROM TrunkChannel c")
public class TrunkChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TRUNKCHANNEL_ID_GENERATOR", sequenceName="trunkchannel_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRUNKCHANNEL_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column
	private LocalDate commissioning;

	/**
	 * Год когда была реализована Гос.программа
	 */
	@Column
	private Integer completed;

	@Column
	private LocalDate decommissioning;

	@Enumerated(EnumType.STRING)
	private ParticipationStatus status;

	//bi-directional many-to-one association to Governmentdevelopmentprogram
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="key_government_program")
	private GovernmentDevelopmentProgram Governmentdevelopmentprogram;

	//bi-directional many-to-one association to Location
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="key_location_end", nullable=false)
	private Location locationEnd;

	//bi-directional many-to-one association to Location
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="key_location_start", nullable=false)
	private Location locationStart;

	//bi-directional many-to-one association to Operator
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="key_operator", nullable=false)
	private Operator Operator;

	//bi-directional many-to-one association to Trunkchanneltype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="key_type_trunk_channel", nullable=false)
	private TypeTrunkChannel type;

}