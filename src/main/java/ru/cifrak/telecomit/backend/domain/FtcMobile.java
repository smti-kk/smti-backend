package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ftc_mobile database table.
 * 
 */
@Entity
@Table(name="ftc_mobile")
@NamedQuery(name="FtcMobile.findAll", query="SELECT f FROM FtcMobile f")
@Data
@EqualsAndHashCode(callSuper = true)
public class FtcMobile extends Ftc implements Serializable {
	@OneToOne()
	@JoinColumn(name="type_id", nullable=false)
	@JsonProperty("type")
	private CatalogsMobiletype catalogsMobileType;

	public FtcMobile() {
	}
}