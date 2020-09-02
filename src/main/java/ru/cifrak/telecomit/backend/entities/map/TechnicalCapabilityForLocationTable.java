package ru.cifrak.telecomit.backend.entities.map;


import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "v_technical_capability_active")
@Data
public class TechnicalCapabilityForLocationTable implements Serializable {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_type_trunkchannel")
    private TypeTrunkChannel trunkChannel;

    @Column(name = "type")
    @Convert(converter = SignalConverter.class)
    private List<Signal> tvOrRadioTypes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_type_mobile")
    private TypeMobile typeMobile;

    @Column(name = "key_location")
    private Integer locationId;

    @Column(name = "type_post")
    @Enumerated(EnumType.STRING)
    private TypePost typePost;

    @Column
    @Enumerated(EnumType.STRING)
    private TcState state;

    private Integer govYearComplete;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceQuality quality;

    public TechnicalCapabilityForLocationTable() {
    }
}
