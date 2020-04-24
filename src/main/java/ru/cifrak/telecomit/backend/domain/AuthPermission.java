package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the auth_permission database table.
 * 
 */
@Entity
@Table(name="auth_permission")
@NamedQuery(name="AuthPermission.findAll", query="SELECT a FROM AuthPermission a")
public class AuthPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="AUTH_PERMISSION_ID_GENERATOR", sequenceName="SEQ_AUTH_PERMISSION")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUTH_PERMISSION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=100)
	private String codename;

	@Column(nullable=false, length=255)
	private String name;

	//bi-directional many-to-one association to AuthGroupPermission
	@OneToMany(mappedBy="authPermission")
	private List<AuthGroupPermission> authGroupPermissions;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="content_type_id", nullable=false)
	private DjangoContentType djangoContentType;

	//bi-directional many-to-one association to UserInfoUserUserPermission
	@OneToMany(mappedBy="authPermission")
	private List<UserInfoUserUserPermission> userInfoUserUserPermissions;

	public AuthPermission() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodename() {
		return this.codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AuthGroupPermission> getAuthGroupPermissions() {
		return this.authGroupPermissions;
	}

	public void setAuthGroupPermissions(List<AuthGroupPermission> authGroupPermissions) {
		this.authGroupPermissions = authGroupPermissions;
	}

	public AuthGroupPermission addAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().add(authGroupPermission);
		authGroupPermission.setAuthPermission(this);

		return authGroupPermission;
	}

	public AuthGroupPermission removeAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().remove(authGroupPermission);
		authGroupPermission.setAuthPermission(null);

		return authGroupPermission;
	}

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

	public List<UserInfoUserUserPermission> getUserInfoUserUserPermissions() {
		return this.userInfoUserUserPermissions;
	}

	public void setUserInfoUserUserPermissions(List<UserInfoUserUserPermission> userInfoUserUserPermissions) {
		this.userInfoUserUserPermissions = userInfoUserUserPermissions;
	}

	public UserInfoUserUserPermission addUserInfoUserUserPermission(UserInfoUserUserPermission userInfoUserUserPermission) {
		getUserInfoUserUserPermissions().add(userInfoUserUserPermission);
		userInfoUserUserPermission.setAuthPermission(this);

		return userInfoUserUserPermission;
	}

	public UserInfoUserUserPermission removeUserInfoUserUserPermission(UserInfoUserUserPermission userInfoUserUserPermission) {
		getUserInfoUserUserPermissions().remove(userInfoUserUserPermission);
		userInfoUserUserPermission.setAuthPermission(null);

		return userInfoUserUserPermission;
	}

}