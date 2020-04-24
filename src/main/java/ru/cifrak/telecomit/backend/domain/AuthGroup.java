package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the auth_group database table.
 * 
 */
@Entity
@Table(name="auth_group")
@NamedQuery(name="AuthGroup.findAll", query="SELECT a FROM AuthGroup a")
public class AuthGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="AUTH_GROUP_ID_GENERATOR", sequenceName="SEQ_AUTH_GROUP")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUTH_GROUP_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=80)
	private String name;

	//bi-directional many-to-one association to AuthGroupPermission
	@OneToMany(mappedBy="authGroup")
	private List<AuthGroupPermission> authGroupPermissions;

	//bi-directional many-to-one association to UserInfoUserGroup
	@OneToMany(mappedBy="authGroup")
	private List<UserInfoUserGroup> userInfoUserGroups;

	public AuthGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		authGroupPermission.setAuthGroup(this);

		return authGroupPermission;
	}

	public AuthGroupPermission removeAuthGroupPermission(AuthGroupPermission authGroupPermission) {
		getAuthGroupPermissions().remove(authGroupPermission);
		authGroupPermission.setAuthGroup(null);

		return authGroupPermission;
	}

	public List<UserInfoUserGroup> getUserInfoUserGroups() {
		return this.userInfoUserGroups;
	}

	public void setUserInfoUserGroups(List<UserInfoUserGroup> userInfoUserGroups) {
		this.userInfoUserGroups = userInfoUserGroups;
	}

	public UserInfoUserGroup addUserInfoUserGroup(UserInfoUserGroup userInfoUserGroup) {
		getUserInfoUserGroups().add(userInfoUserGroup);
		userInfoUserGroup.setAuthGroup(this);

		return userInfoUserGroup;
	}

	public UserInfoUserGroup removeUserInfoUserGroup(UserInfoUserGroup userInfoUserGroup) {
		getUserInfoUserGroups().remove(userInfoUserGroup);
		userInfoUserGroup.setAuthGroup(null);

		return userInfoUserGroup;
	}

}