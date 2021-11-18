package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monitoring_notification")
public class MonitoringNotification {
    @Id
    @SequenceGenerator(name = "MONITORING_NOTIFICATION_ID_GENERATOR", sequenceName = "monitoring_notification_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MONITORING_NOTIFICATION_ID_GENERATOR")
    protected Long id;

    @NonNull
    @OneToOne
    @JoinColumn(name = "key_ap")
    protected AccessPoint ap;

    @NonNull
    @OneToOne
    @JoinColumn(name = "key_user")
    protected User user;

    @NonNull
    @Column(name = "sended")
    protected Boolean sended = false;

    public MonitoringNotification(AccessPoint ap, User user) {
        this.ap = ap;
        this.user = user;
    }
}
