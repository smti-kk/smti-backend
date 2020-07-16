package ru.cifrak.telecomit.backend.entities.map;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.TypeMobile;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "v_technical_capability_active")
@Getter
@Immutable
public class TechnicalCapabilityForLocationTable {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;

    @ManyToOne
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_type_trunkchannel")
    private TypeTrunkChannel trunkChannel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_type_mobile")
    private TypeMobile typeMobile;

    @Column(name = "key_location")
    @JsonIgnore
    private Integer locationId;

    @Column
    @Enumerated(EnumType.STRING)
    private TcState state;

    public TechnicalCapabilityForLocationTable() {
    }
}
