package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the monitoring_accesspoint_traffic database table.
 * 
 */
@Entity
@Table(name="monitoring_accesspoint_traffic")
@NamedQuery(name="MonitoringAccesspointTraffic.findAll", query="SELECT m FROM MonitoringAccesspointTraffic m")
public class MonitoringAccesspointTraffic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MONITORING_ACCESSPOINT_TRAFFIC_ID_GENERATOR", sequenceName="SEQ_MONITORING_ACCESSPOINT_TRAFFIC")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MONITORING_ACCESSPOINT_TRAFFIC_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date day;

	@Column(name="incoming_bytes", nullable=false)
	private Long incomingBytes;

	@Column(name="outgoing_bytes", nullable=false)
	private Long outgoingBytes;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="access_point_id", nullable=false)
	private MonitoringAccesspointRe monitoringAccesspointRe;

	public MonitoringAccesspointTraffic() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDay() {
		return this.day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Long getIncomingBytes() {
		return this.incomingBytes;
	}

	public void setIncomingBytes(Long incomingBytes) {
		this.incomingBytes = incomingBytes;
	}

	public Long getOutgoingBytes() {
		return this.outgoingBytes;
	}

	public void setOutgoingBytes(Long outgoingBytes) {
		this.outgoingBytes = outgoingBytes;
	}

	public MonitoringAccesspointRe getMonitoringAccesspointRe() {
		return this.monitoringAccesspointRe;
	}

	public void setMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		this.monitoringAccesspointRe = monitoringAccesspointRe;
	}

}