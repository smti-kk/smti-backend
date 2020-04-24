package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the capabilities_clarifypetition database table.
 * 
 */
@Entity
@Table(name="capabilities_clarifypetition")
@NamedQuery(name="CapabilitiesClarifypetition.findAll", query="SELECT c FROM CapabilitiesClarifypetition c")
public class CapabilitiesClarifypetition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CAPABILITIES_CLARIFYPETITION_ID_GENERATOR", sequenceName="SEQ_CAPABILITIES_CLARIFYPETITION")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAPABILITIES_CLARIFYPETITION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=2147483647)
	private String comment;

	@Column(nullable=false)
	private Timestamp created;

	@Column(nullable=false)
	private Timestamp started;

	@Column(nullable=false)
	private Integer state;

	private Timestamp updated;

	//bi-directional many-to-one association to CatalogsLocation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="location_id", nullable=false)
	private CatalogsLocation catalogsLocation;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="owner_id", nullable=false)
	private UserInfoUser userInfoUser;

	//bi-directional many-to-one association to FtcAts
	@OneToMany(mappedBy="capabilitiesClarifypetition")
	private List<FtcAts> ftcAts;

	//bi-directional many-to-one association to FtcInternet
	@OneToMany(mappedBy="capabilitiesClarifypetition")
	private List<FtcInternet> ftcInternets;

	//bi-directional many-to-one association to FtcMobile
	@OneToMany(mappedBy="capabilitiesClarifypetition")
	private List<FtcMobile> ftcMobiles;

	//bi-directional many-to-one association to FtcPost
	@OneToMany(mappedBy="capabilitiesClarifypetition")
	private List<FtcPost> ftcPosts;

	//bi-directional many-to-one association to FtcRadio
	@OneToMany(mappedBy="capabilitiesClarifypetition")
	private List<FtcRadio> ftcRadios;

	//bi-directional many-to-one association to FtcTelevision
	@OneToMany(mappedBy="capabilitiesClarifypetition")
	private List<FtcTelevision> ftcTelevisions;

	public CapabilitiesClarifypetition() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getStarted() {
		return this.started;
	}

	public void setStarted(Timestamp started) {
		this.started = started;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
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

	public List<FtcAts> getFtcAts() {
		return this.ftcAts;
	}

	public void setFtcAts(List<FtcAts> ftcAts) {
		this.ftcAts = ftcAts;
	}

	public FtcAts addFtcAts(FtcAts ftcAts) {
		getFtcAts().add(ftcAts);
		ftcAts.setCapabilitiesClarifypetition(this);

		return ftcAts;
	}

	public FtcAts removeFtcAts(FtcAts ftcAts) {
		getFtcAts().remove(ftcAts);
		ftcAts.setCapabilitiesClarifypetition(null);

		return ftcAts;
	}

	public List<FtcInternet> getFtcInternets() {
		return this.ftcInternets;
	}

	public void setFtcInternets(List<FtcInternet> ftcInternets) {
		this.ftcInternets = ftcInternets;
	}

	public FtcInternet addFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().add(ftcInternet);
		ftcInternet.setCapabilitiesClarifypetition(this);

		return ftcInternet;
	}

	public FtcInternet removeFtcInternet(FtcInternet ftcInternet) {
		getFtcInternets().remove(ftcInternet);
		ftcInternet.setCapabilitiesClarifypetition(null);

		return ftcInternet;
	}

	public List<FtcMobile> getFtcMobiles() {
		return this.ftcMobiles;
	}

	public void setFtcMobiles(List<FtcMobile> ftcMobiles) {
		this.ftcMobiles = ftcMobiles;
	}

	public FtcMobile addFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().add(ftcMobile);
		ftcMobile.setCapabilitiesClarifypetition(this);

		return ftcMobile;
	}

	public FtcMobile removeFtcMobile(FtcMobile ftcMobile) {
		getFtcMobiles().remove(ftcMobile);
		ftcMobile.setCapabilitiesClarifypetition(null);

		return ftcMobile;
	}

	public List<FtcPost> getFtcPosts() {
		return this.ftcPosts;
	}

	public void setFtcPosts(List<FtcPost> ftcPosts) {
		this.ftcPosts = ftcPosts;
	}

	public FtcPost addFtcPost(FtcPost ftcPost) {
		getFtcPosts().add(ftcPost);
		ftcPost.setCapabilitiesClarifypetition(this);

		return ftcPost;
	}

	public FtcPost removeFtcPost(FtcPost ftcPost) {
		getFtcPosts().remove(ftcPost);
		ftcPost.setCapabilitiesClarifypetition(null);

		return ftcPost;
	}

	public List<FtcRadio> getFtcRadios() {
		return this.ftcRadios;
	}

	public void setFtcRadios(List<FtcRadio> ftcRadios) {
		this.ftcRadios = ftcRadios;
	}

	public FtcRadio addFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().add(ftcRadio);
		ftcRadio.setCapabilitiesClarifypetition(this);

		return ftcRadio;
	}

	public FtcRadio removeFtcRadio(FtcRadio ftcRadio) {
		getFtcRadios().remove(ftcRadio);
		ftcRadio.setCapabilitiesClarifypetition(null);

		return ftcRadio;
	}

	public List<FtcTelevision> getFtcTelevisions() {
		return this.ftcTelevisions;
	}

	public void setFtcTelevisions(List<FtcTelevision> ftcTelevisions) {
		this.ftcTelevisions = ftcTelevisions;
	}

	public FtcTelevision addFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().add(ftcTelevision);
		ftcTelevision.setCapabilitiesClarifypetition(this);

		return ftcTelevision;
	}

	public FtcTelevision removeFtcTelevision(FtcTelevision ftcTelevision) {
		getFtcTelevisions().remove(ftcTelevision);
		ftcTelevision.setCapabilitiesClarifypetition(null);

		return ftcTelevision;
	}

}