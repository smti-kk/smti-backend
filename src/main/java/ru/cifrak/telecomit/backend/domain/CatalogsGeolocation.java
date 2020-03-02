package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The persistent class for the catalogs_geolocation database table.
 * 
 */
@Entity
@Table(name="catalogs_geolocation")
@NamedQuery(name="CatalogsGeolocation.findAll", query="SELECT c FROM CatalogsGeolocation c")
public class CatalogsGeolocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATALOGS_GEOLOCATION_ID_GENERATOR", sequenceName="catalogs_geolocation_id_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGS_GEOLOCATION_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;


	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	@Column(name="administrative_center")
	private Point administrativeCenter;

//	@JsonIgnore
	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	@Column(name="border")
	private MultiPolygon border;

	@Column(name="fias_code")
	private UUID fiasCode;

	@Column(length=16)
	private String okato;

	@Column(length=16)
	private String oktmo;

	@Column(name="osm_id", length=16)
	private String osmId;

	@JsonBackReference
	//bi-directional many-to-one association to CatalogsLocation
	@OneToMany(mappedBy="catalogsGeolocation")
	private List<CatalogsLocation> catalogsLocations;

	public CatalogsGeolocation() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Point getAdministrativeCenter() {
		return this.administrativeCenter;
	}

	public void setAdministrativeCenter(Point administrativeCenter) {
		this.administrativeCenter = administrativeCenter;
	}

	public MultiPolygon getBorder() {
		return this.border;
	}

	public void setBorder(MultiPolygon border) {
		this.border = border;
	}

	public UUID getFiasCode() {
		return this.fiasCode;
	}

	public void setFiasCode(UUID fiasCode) {
		this.fiasCode = fiasCode;
	}

	public String getOkato() {
		return this.okato;
	}

	public void setOkato(String okato) {
		this.okato = okato;
	}

	public String getOktmo() {
		return this.oktmo;
	}

	public void setOktmo(String oktmo) {
		this.oktmo = oktmo;
	}

	public String getOsmId() {
		return this.osmId;
	}

	public void setOsmId(String osmId) {
		this.osmId = osmId;
	}

	public List<CatalogsLocation> getCatalogsLocations() {
		return this.catalogsLocations;
	}

	public void setCatalogsLocations(List<CatalogsLocation> catalogsLocations) {
		this.catalogsLocations = catalogsLocations;
	}

	public CatalogsLocation addCatalogsLocation(CatalogsLocation catalogsLocation) {
		getCatalogsLocations().add(catalogsLocation);
		catalogsLocation.setCatalogsGeolocation(this);

		return catalogsLocation;
	}

	public CatalogsLocation removeCatalogsLocation(CatalogsLocation catalogsLocation) {
		getCatalogsLocations().remove(catalogsLocation);
		catalogsLocation.setCatalogsGeolocation(null);

		return catalogsLocation;
	}

}