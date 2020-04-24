package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the authtoken_token database table.
 * 
 */
@Entity
@Table(name="authtoken_token")
@NamedQuery(name="AuthtokenToken.findAll", query="SELECT a FROM AuthtokenToken a")
public class AuthtokenToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="AUTHTOKEN_TOKEN_KEY_GENERATOR", sequenceName="SEQ_AUTHTOKEN_TOKEN")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUTHTOKEN_TOKEN_KEY_GENERATOR")
	@Column(unique=true, nullable=false, length=40)
	private String key;

	@Column(nullable=false)
	private Timestamp created;

	//bi-directional many-to-one association to UserInfoUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private UserInfoUser userInfoUser;

	public AuthtokenToken() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public UserInfoUser getUserInfoUser() {
		return this.userInfoUser;
	}

	public void setUserInfoUser(UserInfoUser userInfoUser) {
		this.userInfoUser = userInfoUser;
	}

}