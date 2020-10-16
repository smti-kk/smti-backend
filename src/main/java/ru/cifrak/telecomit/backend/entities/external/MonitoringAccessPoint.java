package ru.cifrak.telecomit.backend.entities.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cifrak.telecomit.backend.entities.APConnectionState;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Параметры из внешних систем для мониторинга.
 * Здесь хранятся параметры от  учетками в UTM5 и Zabbix.
 */
@Data
@NoArgsConstructor

@Entity
@Table(schema = "external_systems")
public class MonitoringAccessPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "MPA_ID_GENERATOR", sequenceName = "mpa_id_seq", allocationSize = 1, schema = "external_systems")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MPA_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    // UTM5 properties
    @Column
    private Integer idUser;

    @Column
    private Integer idAccount;

    @Column
    private Integer idTraffic;

    @Column
    private Integer idService;

    @Column
    private Long lastDayTraffic;

    //TODO:[generate TICKET]: перенести список сетей, которые подвергаются билингу, сейчас они в точках сидят.

    // ZABBIX properties
    /**
     * main service
     */
    @Column
    private Long serviceId;

    // ZABBIX ------------ DEVICE

    @Column
    private String deviceId;
    @Column
    private String deviceName;
    @Column
    private String deviceIp;

    /**
     * High ICMP ping loss
     */
    @Column
    private Long deviceTriggerResponseLost;
    /**
     * High ICMP ping response time
     */
    @Column
    private Long deviceTriggerResponseLow;
    /**
     * Unavailable by ICMP ping
     */
    @Column
    private Long deviceTriggerUnavailable;

    // ZABBIX ------------ SENSOR
    @Column
    private String sensorId;

    @Column
    private String sensorName;

    @Column
    private String sensorIp;

    /**
     * Unavailable by ICMP ping Energy
     */
    @Column
    private Long sensorTriggerEnergy;

    // *******************************************************
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private APConnectionState connectionState = APConnectionState.NOT_MONITORED;
}
