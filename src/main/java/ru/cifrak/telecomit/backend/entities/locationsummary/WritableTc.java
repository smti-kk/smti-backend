package ru.cifrak.telecomit.backend.entities.locationsummary;

import ru.cifrak.telecomit.backend.entities.TcState;

import javax.persistence.*;

@Entity
@Table(name = "technical_capability")
public class WritableTc {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;

    @Column(name = "key_government_program")
    private Integer governmentDevelopmentProgram;

    @Column(name = "key_type_trunkchannel")
    private Integer trunkChannel;

    @Column(name = "key_type_mobile")
    private Integer typeMobile;

    @Column(name = "key_location")
    private Integer locationId;

    @Column
    @Enumerated(EnumType.STRING)
    private TcState state;
}
