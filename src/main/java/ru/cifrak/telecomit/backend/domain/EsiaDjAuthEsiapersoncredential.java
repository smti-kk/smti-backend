package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the esia_dj_auth_esiapersoncredentials database table.
 * 
 */
@Entity
@Table(name="esia_dj_auth_esiapersoncredentials")
@NamedQuery(name="EsiaDjAuthEsiapersoncredential.findAll", query="SELECT e FROM EsiaDjAuthEsiapersoncredential e")
public class EsiaDjAuthEsiapersoncredential implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESIA_DJ_AUTH_ESIAPERSONCREDENTIALS_ID_GENERATOR", sequenceName="SEQ_ESIA_DJ_AUTH_ESIAPERSONCREDENTIALS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESIA_DJ_AUTH_ESIAPERSONCREDENTIALS_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="created_at", nullable=false)
	private Timestamp createdAt;

	@Column(length=2147483647)
	private String email;

	@Column(name="person_birth_date", length=2147483647)
	private String personBirthDate;

	@Column(name="person_first_name", length=2147483647)
	private String personFirstName;

	@Column(name="person_gender", length=2147483647)
	private String personGender;

	@Column(name="person_inn", length=2147483647)
	private String personInn;

	@Column(name="person_last_name", length=2147483647)
	private String personLastName;

	@Column(name="person_middle_name", length=2147483647)
	private String personMiddleName;

	@Column(name="person_passport", length=2147483647)
	private String personPassport;

	@Column(length=2147483647)
	private String phone;

	@Column(length=2147483647)
	private String snils;

	@Column(name="updated_at", nullable=false)
	private Timestamp updatedAt;

	@Column(nullable=false, length=2147483647)
	private String uuid;

	//bi-directional many-to-one association to EsiaDjAuthEsiaauthlogentry
	@OneToMany(mappedBy="esiaDjAuthEsiapersoncredential")
	private List<EsiaDjAuthEsiaauthlogentry> esiaDjAuthEsiaauthlogentries;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="person_account_id")
	private UserInfoUser userInfoUser;

	public EsiaDjAuthEsiapersoncredential() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPersonBirthDate() {
		return this.personBirthDate;
	}

	public void setPersonBirthDate(String personBirthDate) {
		this.personBirthDate = personBirthDate;
	}

	public String getPersonFirstName() {
		return this.personFirstName;
	}

	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getPersonGender() {
		return this.personGender;
	}

	public void setPersonGender(String personGender) {
		this.personGender = personGender;
	}

	public String getPersonInn() {
		return this.personInn;
	}

	public void setPersonInn(String personInn) {
		this.personInn = personInn;
	}

	public String getPersonLastName() {
		return this.personLastName;
	}

	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}

	public String getPersonMiddleName() {
		return this.personMiddleName;
	}

	public void setPersonMiddleName(String personMiddleName) {
		this.personMiddleName = personMiddleName;
	}

	public String getPersonPassport() {
		return this.personPassport;
	}

	public void setPersonPassport(String personPassport) {
		this.personPassport = personPassport;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSnils() {
		return this.snils;
	}

	public void setSnils(String snils) {
		this.snils = snils;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<EsiaDjAuthEsiaauthlogentry> getEsiaDjAuthEsiaauthlogentries() {
		return this.esiaDjAuthEsiaauthlogentries;
	}

	public void setEsiaDjAuthEsiaauthlogentries(List<EsiaDjAuthEsiaauthlogentry> esiaDjAuthEsiaauthlogentries) {
		this.esiaDjAuthEsiaauthlogentries = esiaDjAuthEsiaauthlogentries;
	}

	public EsiaDjAuthEsiaauthlogentry addEsiaDjAuthEsiaauthlogentry(EsiaDjAuthEsiaauthlogentry esiaDjAuthEsiaauthlogentry) {
		getEsiaDjAuthEsiaauthlogentries().add(esiaDjAuthEsiaauthlogentry);
		esiaDjAuthEsiaauthlogentry.setEsiaDjAuthEsiapersoncredential(this);

		return esiaDjAuthEsiaauthlogentry;
	}

	public EsiaDjAuthEsiaauthlogentry removeEsiaDjAuthEsiaauthlogentry(EsiaDjAuthEsiaauthlogentry esiaDjAuthEsiaauthlogentry) {
		getEsiaDjAuthEsiaauthlogentries().remove(esiaDjAuthEsiaauthlogentry);
		esiaDjAuthEsiaauthlogentry.setEsiaDjAuthEsiapersoncredential(null);

		return esiaDjAuthEsiaauthlogentry;
	}

	public UserInfoUser getUserInfoUser() {
		return this.userInfoUser;
	}

	public void setUserInfoUser(UserInfoUser userInfoUser) {
		this.userInfoUser = userInfoUser;
	}

}