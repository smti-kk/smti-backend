package ru.cifrak.telecomit.backend.entities.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

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
    private Integer user;

    @Column
    private Integer account;

    @Column
    private Integer traffic;

    @Column
    private Integer service;

    // ZABBIX properties
    @Column
    private String device;

    @Column
    private String sensor;

}
