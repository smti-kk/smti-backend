package ru.cifrak.telecomit.backend.entities.map;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.entities.TypeMobile;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;

import javax.persistence.*;

@Entity
@Table(name = "v_technical_capability_active")
@Data
public class TechnicalCapabilityForLocationTable {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "key_type_trunkchannel")
    private TypeTrunkChannel trunkChannel;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "key_type_mobile")
    private TypeMobile typeMobile;

    @Column(name = "key_location")
    private Integer locationId;

    @Column
    @Enumerated(EnumType.STRING)
    private TcState state;

    public TechnicalCapabilityForLocationTable() {
    }
}
