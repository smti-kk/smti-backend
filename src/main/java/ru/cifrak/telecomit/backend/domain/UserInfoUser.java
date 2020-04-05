package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the user_info_user database table.
 * 
 */
@Entity
@Table(name="user_info_user")
@NamedQuery(name="UserInfoUser.findAll", query="SELECT u FROM UserInfoUser u")
public class UserInfoUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_INFO_USER_ID_GENERATOR", sequenceName="SEQ_USER_INFO_USER")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_INFO_USER_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String contacts;

	@Column(nullable=false, length=254)
	private String email;

	@Column(length=256)
	private String fio;

	@Column(name="is_active", nullable=false)
	private Boolean isActive;

	@Column(name="is_staff", nullable=false)
	private Boolean isStaff;

	@Column(name="is_superuser", nullable=false)
	private Boolean isSuperuser;

	@Column(name="last_login")
	private Timestamp lastLogin;

	@Column(nullable=false, length=128)
	private String password;

	@Column(nullable=false)
	private Integer role;

	//bi-directional many-to-one association to AuthtokenToken
	@OneToMany(mappedBy="userInfoUser")
	private List<AuthtokenToken> authtokenTokens;

	//bi-directional many-to-one association to CapabilitiesClarifypetition
	@OneToMany(mappedBy="userInfoUser")
	private List<CapabilitiesClarifypetition> capabilitiesClarifypetitions;

	//bi-directional many-to-one association to CapabilitiesPetition
	@OneToMany(mappedBy="userInfoUser")
	private List<CapabilitiesPetition> capabilitiesPetitions;

	//bi-directional many-to-one association to DjangoAdminLog
	@OneToMany(mappedBy="userInfoUser")
	private List<DjangoAdminLog> djangoAdminLogs;

	//bi-directional many-to-one association to EsiaDjAuthEsiapersoncredential
	@OneToMany(mappedBy="userInfoUser")
	private List<EsiaDjAuthEsiapersoncredential> esiaDjAuthEsiapersoncredentials;

	//bi-directional many-to-one association to CatalogsOperator
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operator_id")
	private CatalogsOperator catalogsOperator;

	//bi-directional many-to-one association to UserInfoUserGroup
	@OneToMany(mappedBy="userInfoUser")
	private List<UserInfoUserGroup> userInfoUserGroups;

	//bi-directional many-to-one association to UserInfoUserLocation
	@OneToMany(mappedBy="userInfoUser")
	private List<UserInfoUserLocation> userInfoUserLocations;

	//bi-directional many-to-one association to UserInfoUserUserPermission
	@OneToMany(mappedBy="userInfoUser")
	private List<UserInfoUserUserPermission> userInfoUserUserPermissions;

	public UserInfoUser() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContacts() {
		return this.contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFio() {
		return this.fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsStaff() {
		return this.isStaff;
	}

	public void setIsStaff(Boolean isStaff) {
		this.isStaff = isStaff;
	}

	public Boolean getIsSuperuser() {
		return this.isSuperuser;
	}

	public void setIsSuperuser(Boolean isSuperuser) {
		this.isSuperuser = isSuperuser;
	}

	public Timestamp getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRole() {
		return this.role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public List<AuthtokenToken> getAuthtokenTokens() {
		return this.authtokenTokens;
	}

	public void setAuthtokenTokens(List<AuthtokenToken> authtokenTokens) {
		this.authtokenTokens = authtokenTokens;
	}

	public AuthtokenToken addAuthtokenToken(AuthtokenToken authtokenToken) {
		getAuthtokenTokens().add(authtokenToken);
		authtokenToken.setUserInfoUser(this);

		return authtokenToken;
	}

	public AuthtokenToken removeAuthtokenToken(AuthtokenToken authtokenToken) {
		getAuthtokenTokens().remove(authtokenToken);
		authtokenToken.setUserInfoUser(null);

		return authtokenToken;
	}

	public List<CapabilitiesClarifypetition> getCapabilitiesClarifypetitions() {
		return this.capabilitiesClarifypetitions;
	}

	public void setCapabilitiesClarifypetitions(List<CapabilitiesClarifypetition> capabilitiesClarifypetitions) {
		this.capabilitiesClarifypetitions = capabilitiesClarifypetitions;
	}

	public CapabilitiesClarifypetition addCapabilitiesClarifypetition(CapabilitiesClarifypetition capabilitiesClarifypetition) {
		getCapabilitiesClarifypetitions().add(capabilitiesClarifypetition);
		capabilitiesClarifypetition.setUserInfoUser(this);

		return capabilitiesClarifypetition;
	}

	public CapabilitiesClarifypetition removeCapabilitiesClarifypetition(CapabilitiesClarifypetition capabilitiesClarifypetition) {
		getCapabilitiesClarifypetitions().remove(capabilitiesClarifypetition);
		capabilitiesClarifypetition.setUserInfoUser(null);

		return capabilitiesClarifypetition;
	}

	public List<CapabilitiesPetition> getCapabilitiesPetitions() {
		return this.capabilitiesPetitions;
	}

	public void setCapabilitiesPetitions(List<CapabilitiesPetition> capabilitiesPetitions) {
		this.capabilitiesPetitions = capabilitiesPetitions;
	}

	public CapabilitiesPetition addCapabilitiesPetition(CapabilitiesPetition capabilitiesPetition) {
		getCapabilitiesPetitions().add(capabilitiesPetition);
		capabilitiesPetition.setUserInfoUser(this);

		return capabilitiesPetition;
	}

	public CapabilitiesPetition removeCapabilitiesPetition(CapabilitiesPetition capabilitiesPetition) {
		getCapabilitiesPetitions().remove(capabilitiesPetition);
		capabilitiesPetition.setUserInfoUser(null);

		return capabilitiesPetition;
	}

	public List<DjangoAdminLog> getDjangoAdminLogs() {
		return this.djangoAdminLogs;
	}

	public void setDjangoAdminLogs(List<DjangoAdminLog> djangoAdminLogs) {
		this.djangoAdminLogs = djangoAdminLogs;
	}

	public DjangoAdminLog addDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().add(djangoAdminLog);
		djangoAdminLog.setUserInfoUser(this);

		return djangoAdminLog;
	}

	public DjangoAdminLog removeDjangoAdminLog(DjangoAdminLog djangoAdminLog) {
		getDjangoAdminLogs().remove(djangoAdminLog);
		djangoAdminLog.setUserInfoUser(null);

		return djangoAdminLog;
	}

	public List<EsiaDjAuthEsiapersoncredential> getEsiaDjAuthEsiapersoncredentials() {
		return this.esiaDjAuthEsiapersoncredentials;
	}

	public void setEsiaDjAuthEsiapersoncredentials(List<EsiaDjAuthEsiapersoncredential> esiaDjAuthEsiapersoncredentials) {
		this.esiaDjAuthEsiapersoncredentials = esiaDjAuthEsiapersoncredentials;
	}

	public EsiaDjAuthEsiapersoncredential addEsiaDjAuthEsiapersoncredential(EsiaDjAuthEsiapersoncredential esiaDjAuthEsiapersoncredential) {
		getEsiaDjAuthEsiapersoncredentials().add(esiaDjAuthEsiapersoncredential);
		esiaDjAuthEsiapersoncredential.setUserInfoUser(this);

		return esiaDjAuthEsiapersoncredential;
	}

	public EsiaDjAuthEsiapersoncredential removeEsiaDjAuthEsiapersoncredential(EsiaDjAuthEsiapersoncredential esiaDjAuthEsiapersoncredential) {
		getEsiaDjAuthEsiapersoncredentials().remove(esiaDjAuthEsiapersoncredential);
		esiaDjAuthEsiapersoncredential.setUserInfoUser(null);

		return esiaDjAuthEsiapersoncredential;
	}

	public CatalogsOperator getCatalogsOperator() {
		return this.catalogsOperator;
	}

	public void setCatalogsOperator(CatalogsOperator catalogsOperator) {
		this.catalogsOperator = catalogsOperator;
	}

	public List<UserInfoUserGroup> getUserInfoUserGroups() {
		return this.userInfoUserGroups;
	}

	public void setUserInfoUserGroups(List<UserInfoUserGroup> userInfoUserGroups) {
		this.userInfoUserGroups = userInfoUserGroups;
	}

	public UserInfoUserGroup addUserInfoUserGroup(UserInfoUserGroup userInfoUserGroup) {
		getUserInfoUserGroups().add(userInfoUserGroup);
		userInfoUserGroup.setUserInfoUser(this);

		return userInfoUserGroup;
	}

	public UserInfoUserGroup removeUserInfoUserGroup(UserInfoUserGroup userInfoUserGroup) {
		getUserInfoUserGroups().remove(userInfoUserGroup);
		userInfoUserGroup.setUserInfoUser(null);

		return userInfoUserGroup;
	}

	public List<UserInfoUserLocation> getUserInfoUserLocations() {
		return this.userInfoUserLocations;
	}

	public void setUserInfoUserLocations(List<UserInfoUserLocation> userInfoUserLocations) {
		this.userInfoUserLocations = userInfoUserLocations;
	}

	public UserInfoUserLocation addUserInfoUserLocation(UserInfoUserLocation userInfoUserLocation) {
		getUserInfoUserLocations().add(userInfoUserLocation);
		userInfoUserLocation.setUserInfoUser(this);

		return userInfoUserLocation;
	}

	public UserInfoUserLocation removeUserInfoUserLocation(UserInfoUserLocation userInfoUserLocation) {
		getUserInfoUserLocations().remove(userInfoUserLocation);
		userInfoUserLocation.setUserInfoUser(null);

		return userInfoUserLocation;
	}

	public List<UserInfoUserUserPermission> getUserInfoUserUserPermissions() {
		return this.userInfoUserUserPermissions;
	}

	public void setUserInfoUserUserPermissions(List<UserInfoUserUserPermission> userInfoUserUserPermissions) {
		this.userInfoUserUserPermissions = userInfoUserUserPermissions;
	}

	public UserInfoUserUserPermission addUserInfoUserUserPermission(UserInfoUserUserPermission userInfoUserUserPermission) {
		getUserInfoUserUserPermissions().add(userInfoUserUserPermission);
		userInfoUserUserPermission.setUserInfoUser(this);

		return userInfoUserUserPermission;
	}

	public UserInfoUserUserPermission removeUserInfoUserUserPermission(UserInfoUserUserPermission userInfoUserUserPermission) {
		getUserInfoUserUserPermissions().remove(userInfoUserUserPermission);
		userInfoUserUserPermission.setUserInfoUser(null);

		return userInfoUserUserPermission;
	}

}