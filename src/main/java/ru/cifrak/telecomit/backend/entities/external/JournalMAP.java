package ru.cifrak.telecomit.backend.entities.external;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.Auditing;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Свзяка нашей Точки доступа и Мониторинговых параметров, а также статус, Активно/Неактивно
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor

@Entity
@Table(schema = "external_systems")
public class JournalMAP extends Auditing implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "JMPA_ID_GENERATOR", sequenceName = "jmpa_id_seq", allocationSize = 1, schema = "external_systems")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JMPA_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "key_ap")
    private AccessPoint ap;

    @ManyToOne
    @JoinColumn(name = "key_map")
    private MonitoringAccessPoint map;

    @Column
    private Boolean active;
}
