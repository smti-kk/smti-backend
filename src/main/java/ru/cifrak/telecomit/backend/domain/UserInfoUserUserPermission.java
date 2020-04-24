package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_info_user_user_permissions database table.
 * 
 */
@Entity
@Table(name="user_info_user_user_permissions")
@NamedQuery(name="UserInfoUserUserPermission.findAll", query="SELECT u FROM UserInfoUserUserPermission u")
public class UserInfoUserUserPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_INFO_USER_USER_PERMISSIONS_ID_GENERATOR", sequenceName="SEQ_USER_INFO_USER_USER_PERMISSIONS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_INFO_USER_USER_PERMISSIONS_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to AuthPermission
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="permission_id", nullable=false)
	private AuthPermission authPermission;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private UserInfoUser userInfoUser;

	public UserInfoUserUserPermission() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuthPermission getAuthPermission() {
		return this.authPermission;
	}

	public void setAuthPermission(AuthPermission authPermission) {
		this.authPermission = authPermission;
	}

	public UserInfoUser getUserInfoUser() {
		return this.userInfoUser;
	}

	public void setUserInfoUser(UserInfoUser userInfoUser) {
		this.userInfoUser = userInfoUser;
	}

}