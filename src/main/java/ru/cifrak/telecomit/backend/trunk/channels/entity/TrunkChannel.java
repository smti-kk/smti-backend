package ru.cifrak.telecomit.backend.trunk.channels.entity;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.entities.TypeTrunkChannel;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;

import javax.persistence.*;
import java.time.LocalDate;

@NamedEntityGraphs(
        @NamedEntityGraph(
                name = "trunk_channel_full",
                attributeNodes = {
                }
        )
)
@Entity
@Data
public class TrunkChannel {
    @Id
    @SequenceGenerator(name = "TRUNKCHANNEL_ID_GENERATOR", sequenceName = "trunkchannel_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRUNKCHANNEL_ID_GENERATOR")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "key_location_start")
    private MapLocation locationStart;

    @ManyToOne
    @JoinColumn(name = "key_location_end", nullable = false)
    private MapLocation locationEnd;

    @ManyToOne
    @JoinColumn(name = "key_operator", nullable = false)
    private Operator operator;

    @ManyToOne
    @JoinColumn(name = "key_type_trunk_channel", nullable = false)
    private TypeTrunkChannel typeTrunkChannel;

    private LocalDate commissioning;

    private LocalDate decommissioning;

    @ManyToOne
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram program;

    private Integer completed;
}
