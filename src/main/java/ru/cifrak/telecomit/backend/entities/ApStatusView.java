package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor

@Entity
@Table(name = "ap_status_view")
public class ApStatusView {
    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "key_ap")
    private AccessPoint ap;

    @Column
    @Enumerated(EnumType.STRING)
    private APConnectionState connectionState;
}
