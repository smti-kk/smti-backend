package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the django_migrations database table.
 * 
 */
@Entity
@Table(name="django_migrations")
@NamedQuery(name="DjangoMigration.findAll", query="SELECT d FROM DjangoMigration d")
public class DjangoMigration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DJANGO_MIGRATIONS_ID_GENERATOR", sequenceName="SEQ_DJANGO_MIGRATIONS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DJANGO_MIGRATIONS_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=255)
	private String app;

	@Column(nullable=false)
	private Timestamp applied;

	@Column(nullable=false, length=255)
	private String name;

	public DjangoMigration() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApp() {
		return this.app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Timestamp getApplied() {
		return this.applied;
	}

	public void setApplied(Timestamp applied) {
		this.applied = applied;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}