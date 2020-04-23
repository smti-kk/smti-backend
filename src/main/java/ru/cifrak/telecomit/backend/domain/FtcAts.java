package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "ftc_ats")
@NamedQuery(name = "FtcAts.findAll", query = "SELECT f FROM FtcAts f")
@Data
@EqualsAndHashCode(callSuper = true)
public class FtcAts extends Ftc implements Serializable {
    @Column(name = "quantity_payphone", nullable = false)
    @JsonProperty("quantity_payphone")
    private Integer quantityPayphone;

	@Column(nullable=false)
	private Integer capacity;
}