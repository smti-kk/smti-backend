package ru.cifrak.telecomit.backend.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the monitoring_connectionmetric database table.
 * 
 */
@Entity
@Table(name="monitoring_connectionmetric")
@NamedQuery(name="MonitoringConnectionmetric.findAll", query="SELECT m FROM MonitoringConnectionMetric m")
public class MonitoringConnectionMetric implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MONITORING_CONNECTIONMETRIC_ID_GENERATOR", sequenceName="SEQ_MONITORING_CONNECTIONMETRIC")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MONITORING_CONNECTIONMETRIC_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private Boolean available;

	@Column(name="medium_speed_per_hour", nullable=false)
	private double mediumSpeedPerHour;

	@Column(name="time_mark", nullable=false)
	private Timestamp timeMark;

	//bi-directional many-to-one association to MonitoringAccesspointRe
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="access_point_id", nullable=false)
	private MonitoringAccesspointRe monitoringAccesspointRe;

	public MonitoringConnectionMetric() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getAvailable() {
		return this.available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public double getMediumSpeedPerHour() {
		return this.mediumSpeedPerHour;
	}

	public void setMediumSpeedPerHour(double mediumSpeedPerHour) {
		this.mediumSpeedPerHour = mediumSpeedPerHour;
	}

	public Timestamp getTimeMark() {
		return this.timeMark;
	}

	public void setTimeMark(Timestamp timeMark) {
		this.timeMark = timeMark;
	}

	public MonitoringAccesspointRe getMonitoringAccesspointRe() {
		return this.monitoringAccesspointRe;
	}

	public void setMonitoringAccesspointRe(MonitoringAccesspointRe monitoringAccesspointRe) {
		this.monitoringAccesspointRe = monitoringAccesspointRe;
	}

}