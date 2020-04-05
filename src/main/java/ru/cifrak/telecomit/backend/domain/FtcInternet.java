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
 * The persistent class for the ftc_internet database table.
 * 
 */
@Entity
@Table(name="ftc_internet")
@NamedQuery(name="FtcInternet.findAll", query="SELECT f FROM FtcInternet f")
@Data
@EqualsAndHashCode(callSuper = true)
public class FtcInternet extends AccessPoint implements Serializable {
	//bi-directional many-to-one association to CatalogsTrunkchanneltype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_trunkchannel_id", nullable=false)
	@JsonProperty("type_trunkchannel")
	private CatalogsTrunkChannelType trunkChannelType;
}