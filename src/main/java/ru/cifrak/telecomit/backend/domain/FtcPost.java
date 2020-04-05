package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ftc_post database table.
 * 
 */
@Entity
@Table(name="ftc_post")
@NamedQuery(name="FtcPost.findAll", query="SELECT f FROM FtcPost f")
@Data
@EqualsAndHashCode(callSuper = true)
public class FtcPost extends AccessPoint implements Serializable {
	@Column(nullable=false, length=4)
	private String type;
}