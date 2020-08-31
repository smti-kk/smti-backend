package ru.cifrak.telecomit.backend.entities.external;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    //TODO:[generate TICKET]: перенести список сетей, которые подвергаются билингу, сейчас они в точках сидят.

    // ZABBIX properties

    // ZABBIX ------------ DEVICE

    @Column
    private String idDevice;
    @Column
    private String nameDevice;
    @Column
    private String ipDevice;

    /**
     * High ICMP ping loss
     */
    @Column
    private String idT1;
    /**
     * High ICMP ping response time
     */
    @Column
    private String idT2;
    /**
     * Unavailable by ICMP ping
     */
    @Column
    private String idT3;
    // ZABBIX ------------ SENSOR
    @Column
    private String idSensor;

    @Column
    private String nameSensor;

    @Column
    private String ipSensor;

    /**
     * Unavailable by ICMP ping Energy
     */
    @Column
    private String idT4;


}
