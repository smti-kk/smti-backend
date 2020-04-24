package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the capabilities_petition database table.
 * 
 */
@Entity
@Table(name="capabilities_petition")
@NamedQuery(name="CapabilitiesPetition.findAll", query="SELECT c FROM CapabilitiesPetition c")
public class CapabilitiesPetition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CAPABILITIES_PETITION_ID_GENERATOR", sequenceName="SEQ_CAPABILITIES_PETITION")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAPABILITIES_PETITION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="answer_file", length=100)
	private String answerFile;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Temporal(TemporalType.DATE)
	@Column(name="date_petition", nullable=false)
	private Date datePetition;

	@Column(nullable=false)
	private Integer level;

	@Column(name="petition_file", length=100)
	private String petitionFile;

	@Column(nullable=false)
	private Integer priority;

	@Column(nullable=false)
	private Integer state;

	@Column(nullable=false, length=256)
	private String title;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id")
	private CatalogsLocation catalogsLocation;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="owner_id")
	private UserInfoUser userInfoUser;

	public CapabilitiesPetition() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswerFile() {
		return this.answerFile;
	}

	public void setAnswerFile(String answerFile) {
		this.answerFile = answerFile;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Date getDatePetition() {
		return this.datePetition;
	}

	public void setDatePetition(Date datePetition) {
		this.datePetition = datePetition;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getPetitionFile() {
		return this.petitionFile;
	}

	public void setPetitionFile(String petitionFile) {
		this.petitionFile = petitionFile;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CatalogsLocation getCatalogsLocation() {
		return this.catalogsLocation;
	}

	public void setCatalogsLocation(CatalogsLocation catalogsLocation) {
		this.catalogsLocation = catalogsLocation;
	}

	public UserInfoUser getUserInfoUser() {
		return this.userInfoUser;
	}

	public void setUserInfoUser(UserInfoUser userInfoUser) {
		this.userInfoUser = userInfoUser;
	}

}