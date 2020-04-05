package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the django_content_type database table.
 * 
 */
@Entity
@Table(name="django_content_type")
@NamedQuery(name="DjangoContentType.findAll", query="SELECT d FROM DjangoContentType d")
public class DjangoContentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DJANGO_CONTENT_TYPE_ID_GENERATOR", sequenceName="SEQ_DJANGO_CONTENT_TYPE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DJANGO_CONTENT_TYPE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="app_label", nullable=false, length=100)
	private String appLabel;

	@Column(nullable=false, length=100)
	private String model;

	//bi-directional many-to-one association to AuthPermission
	@OneToMany(mappedBy="djangoContentType")
	private List<AuthPermission> authPermissions;

	//bi-directional many-to-one association to DjangoAdminLog
	@OneToMany(mappedBy="djangoContentType")
	private List<DjangoAdminLog> djangoAdminLogs;

	public DjangoContentType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppLabel() {
		return this.appLabel;
	}

	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<AuthPermission> getAuthPermissions() {
		return this.authPermissions;
	}

	public void setAuthPermissions(List<AuthPermission> authPermissions) {
		this.authPermissions = authPermissions;
	}

	public AuthPermission addAuthPermission(AuthPermission authPermission) {
		getAuthPermissions().add(authPermission);
		authPermission.setDjangoContentType(this);

		return authPermission;
	}

	public AuthPermission removeAuthPermission(AuthPermission authPermission) {
		getAuthPermissions().remove(authPermission);
		authPermission.setDjangoContentType(null);

		return authPermission;
	}

	public List<DjangoAdminLog> getDjangoAdminLogs() {
		return this.djangoAdminLogs;
	}

	public void setDjangoAdminLogs(List<DjangoAdminLog> djangoAdminLogs) {
		this.djangoAdminLogs = djangoAdminLogs;
	}

	public DjangoAdminLog addDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().add(djangoAdminLog);
		djangoAdminLog.setDjangoContentType(this);

		return djangoAdminLog;
	}

	public DjangoAdminLog removeDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().remove(djangoAdminLog);
		djangoAdminLog.setDjangoContentType(null);

		return djangoAdminLog;
	}

}