package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_info_user_groups database table.
 * 
 */
@Entity
@Table(name="user_info_user_groups")
@NamedQuery(name="UserInfoUserGroup.findAll", query="SELECT u FROM UserInfoUserGroup u")
public class UserInfoUserGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_INFO_USER_GROUPS_ID_GENERATOR", sequenceName="SEQ_USER_INFO_USER_GROUPS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_INFO_USER_GROUPS_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to AuthGroup
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_id", nullable=false)
	private AuthGroup authGroup;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private UserInfoUser userInfoUser;

	public UserInfoUserGroup() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuthGroup getAuthGroup() {
		return this.authGroup;
	}

	public void setAuthGroup(AuthGroup authGroup) {
		this.authGroup = authGroup;
	}

	public UserInfoUser getUserInfoUser() {
		return this.userInfoUser;
	}

	public void setUserInfoUser(UserInfoUser userInfoUser) {
		this.userInfoUser = userInfoUser;
	}

}