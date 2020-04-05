package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the django_admin_log database table.
 * 
 */
@Entity
@Table(name="django_admin_log")
@NamedQuery(name="DjangoAdminLog.findAll", query="SELECT d FROM DjangoAdminLog d")
public class DjangoAdminLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DJANGO_ADMIN_LOG_ID_GENERATOR", sequenceName="SEQ_DJANGO_ADMIN_LOG")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DJANGO_ADMIN_LOG_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="action_flag", nullable=false)
	private Integer actionFlag;

	@Column(name="action_time", nullable=false)
	private Timestamp actionTime;

	@Column(name="change_message", nullable=false, length=2147483647)
	private String changeMessage;

	@Column(name="object_id", length=2147483647)
	private String objectId;

	@Column(name="object_repr", nullable=false, length=200)
	private String objectRepr;

	//bi-directional many-to-one association to DjangoContentType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="content_type_id")
	private DjangoContentType djangoContentType;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private UserInfoUser userInfoUser;

	public DjangoAdminLog() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActionFlag() {
		return this.actionFlag;
	}

	public void setActionFlag(Integer actionFlag) {
		this.actionFlag = actionFlag;
	}

	public Timestamp getActionTime() {
		return this.actionTime;
	}

	public void setActionTime(Timestamp actionTime) {
		this.actionTime = actionTime;
	}

	public String getChangeMessage() {
		return this.changeMessage;
	}

	public void setChangeMessage(String changeMessage) {
		this.changeMessage = changeMessage;
	}

	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectRepr() {
		return this.objectRepr;
	}

	public void setObjectRepr(String objectRepr) {
		this.objectRepr = objectRepr;
	}

	public DjangoContentType getDjangoContentType() {
		return this.djangoContentType;
	}

	public void setDjangoContentType(DjangoContentType djangoContentType) {
		this.djangoContentType = djangoContentType;
	}

	public UserInfoUser getUserInfoUser() {
		return this.userInfoUser;
	}

	public void setUserInfoUser(UserInfoUser userInfoUser) {
		this.userInfoUser = userInfoUser;
	}

}