package ru.cifrak.telecomit.backend.domain;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the catalogs_trunkchanneltype database table.
 * 
 */
@Entity
@Table(name="catalogs_trunkchanneltype")
@NamedQuery(name="CatalogsTrunkchanneltype.findAll", query="SELECT c FROM CatalogsTrunkChannelType c")
@Data
public class CatalogsTrunkChannelType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_TRUNKCHANNELTYPE_ID_GENERATOR", sequenceName="SEQ_CATALOGS_TRUNKCHANNELTYPE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_TRUNKCHANNELTYPE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false, length=256)
	private String name;

	@Column(name="report_weight", nullable=false)
	private Integer reportWeight;
}