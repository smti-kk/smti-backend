package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_info_user_locations database table.
 * 
 */
@Entity
@Table(name="user_info_user_locations")
@NamedQuery(name="UserInfoUserLocation.findAll", query="SELECT u FROM UserInfoUserLocation u")
public class UserInfoUserLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_INFO_USER_LOCATIONS_ID_GENERATOR", sequenceName="SEQ_USER_INFO_USER_LOCATIONS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_INFO_USER_LOCATIONS_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id", nullable=false)
	private CatalogsLocation catalogsLocation;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private UserInfoUser userInfoUser;

	public UserInfoUserLocation() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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