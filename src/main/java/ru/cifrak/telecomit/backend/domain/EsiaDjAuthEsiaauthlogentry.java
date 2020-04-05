package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the esia_dj_auth_esiaauthlogentry database table.
 * 
 */
@Entity
@Table(name="esia_dj_auth_esiaauthlogentry")
@NamedQuery(name="EsiaDjAuthEsiaauthlogentry.findAll", query="SELECT e FROM EsiaDjAuthEsiaauthlogentry e")
public class EsiaDjAuthEsiaauthlogentry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESIA_DJ_AUTH_ESIAAUTHLOGENTRY_ID_GENERATOR", sequenceName="SEQ_ESIA_DJ_AUTH_ESIAAUTHLOGENTRY")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESIA_DJ_AUTH_ESIAAUTHLOGENTRY_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private Boolean newbie;

	@Column(nullable=false)
	private Boolean success;

	@Column(nullable=false)
	private Timestamp timestamp;

	@Column(name="user_snils", length=2147483647)
	private String userSnils;

	@Column(name="user_uid", nullable=false, length=2147483647)
	private String userUid;

	//bi-directional many-to-one association to EsiaDjAuthEsiapersoncredential
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="credentials_id")
	private EsiaDjAuthEsiapersoncredential esiaDjAuthEsiapersoncredential;

	public EsiaDjAuthEsiaauthlogentry() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getNewbie() {
		return this.newbie;
	}

	public void setNewbie(Boolean newbie) {
		this.newbie = newbie;
	}

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getUserSnils() {
		return this.userSnils;
	}

	public void setUserSnils(String userSnils) {
		this.userSnils = userSnils;
	}

	public String getUserUid() {
		return this.userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public EsiaDjAuthEsiapersoncredential getEsiaDjAuthEsiapersoncredential() {
		return this.esiaDjAuthEsiapersoncredential;
	}

	public void setEsiaDjAuthEsiapersoncredential(EsiaDjAuthEsiapersoncredential esiaDjAuthEsiapersoncredential) {
		this.esiaDjAuthEsiapersoncredential = esiaDjAuthEsiapersoncredential;
	}

}