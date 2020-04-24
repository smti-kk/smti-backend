package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the databasechangeloglock database table.
 * 
 */
@Entity
@Table(name="databasechangeloglock")
@NamedQuery(name="Databasechangeloglock.findAll", query="SELECT d FROM Databasechangeloglock d")
public class Databasechangeloglock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DATABASECHANGELOGLOCK_ID_GENERATOR", sequenceName="SEQ_DATABASECHANGELOGLOCK")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DATABASECHANGELOGLOCK_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private Boolean locked;

	@Column(length=255)
	private String lockedby;

	private Timestamp lockgranted;

	public Databasechangeloglock() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getLocked() {
		return this.locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getLockedby() {
		return this.lockedby;
	}

	public void setLockedby(String lockedby) {
		this.lockedby = lockedby;
	}

	public Timestamp getLockgranted() {
		return this.lockgranted;
	}

	public void setLockgranted(Timestamp lockgranted) {
		this.lockgranted = lockgranted;
	}

}