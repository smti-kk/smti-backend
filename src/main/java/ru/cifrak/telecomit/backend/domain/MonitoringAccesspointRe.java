package ru.cifrak.telecomit.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the monitoring_accesspoint_re database table.
 */
@Data
@Entity
@Table(name = "monitoring_accesspoint_re")
public class MonitoringAccesspointRe implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MONITORING_ACCESSPOINT_RE_ID_GENERATOR", sequenceName = "SEQ_MONITORING_ACCESSPOINT_RE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MONITORING_ACCESSPOINT_RE_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

	@Column(length=1000)
	private String address;

	@Column(name="billing_id")
	@JsonProperty("billing_id")
	private Integer billingId;

	private Integer completed;

	@Column(length=500)
	private String contractor;

	@Column(name="created_at", nullable=false)
	@JsonProperty("created_at")
	private Timestamp createdAt;

	@Column(length=500)
	private String customer;

	@Column(name="defined_speed", length=500)
	@JsonProperty("defined_speed")
	private String definedSpeed;

	@Column(length=2147483647)
	private String description;

	@Column(name="ip_config", length=2147483647)
	@JsonProperty("ip_config")
	private String ipConfig;

	@Column(name="max_amount")
	@JsonProperty("max_amount")
	private Integer maxAmount;

	@Column(length=500)
	private String name;

	@Column(name="net_traffic_last_month")
	@JsonProperty("net_traffic_last_month")
	private Long netTrafficLastMonth;

	@Column(name="net_traffic_last_week")
	@JsonProperty("net_traffic_last_week")
	private Long netTrafficLastWeek;

	@Column(length=500)
	private String node;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(nullable = false)
    private Point point;

	@Column(length=6)
	private String quality;

	@Column(length=4)
	private String state;

	private Integer ucn;

	@Column(name="updated_at", nullable=false)
	@JsonProperty("updated_at")
	private Timestamp updatedAt;

	@Column(nullable=false)
	private Boolean visible;

	//bi-directional many-to-one association to CatalogsContract
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contract_id")
	@JsonProperty("catalogs_contract")
	@JsonIgnore
	private CatalogsContract catalogsContract;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="government_program_id")
	@JsonProperty("government_program")
	private CatalogsGovernmentDevelopmentProgram catalogsGovernmentDevelopmentProgram;

	//bi-directional many-to-one association to CatalogsInternetaccesstype
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="connection_type_id")
	@JsonProperty("connection_type")
	private CatalogsInternetaccesstype catalogsInternetaccesstype;

	//bi-directional many-to-one association to CatalogsOperator
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operator_id")
	@JsonProperty("catalogs_operator")
	private CatalogsOperator catalogsOperator;

	//bi-directional many-to-one association to CatalogsOrganization
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organization_id")
	@JsonBackReference
	private CatalogsOrganization catalogsOrganization;

	//bi-directional many-to-one association to MonitoringAccesspointTraffic
	@OneToMany(mappedBy="monitoringAccesspointRe")
	@JsonIgnore
	private List<MonitoringAccesspointTraffic> monitoringAccesspointTraffics;

    //bi-directional many-to-one association to MonitoringConnectionmetric
    @OneToMany(mappedBy = "monitoringAccesspointRe")
	@JsonIgnore
    private List<MonitoringConnectionMetric> monitoringConnectionMetrics;

    public MonitoringAccesspointRe() {
    }
}