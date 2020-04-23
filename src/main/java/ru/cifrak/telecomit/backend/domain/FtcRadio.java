package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ftc_radio database table.
 * 
 */
@Entity
@Table(name="ftc_radio")
@NamedQuery(name="FtcRadio.findAll", query="SELECT f FROM FtcRadio f")
@Data
@EqualsAndHashCode(callSuper = true)
public class FtcRadio extends Ftc implements Serializable {
	@Column(nullable=false)
	private Integer type;
}