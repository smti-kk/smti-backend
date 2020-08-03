package ru.cifrak.telecomit.backend.entities.locationsummary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import ru.cifrak.telecomit.backend.entities.ServiceQuality;
import ru.cifrak.telecomit.backend.entities.Signal;
import ru.cifrak.telecomit.backend.entities.TcState;
import ru.cifrak.telecomit.backend.serializer.IntegerSignalConverter;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;
import ru.cifrak.telecomit.backend.serializer.SignalDeserializer;

import javax.persistence.*;
import java.time.Year;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "technical_capability")
@Data
@AllArgsConstructor
public class WritableTc {
    @Id
    @SequenceGenerator(name = "TECHNICAL_CAPABILITY_GENERATOR", sequenceName = "technical_capability_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TECHNICAL_CAPABILITY_GENERATOR")
    private Integer id;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceQuality quality;

    @Column
    @Enumerated(EnumType.STRING)
    private TcState state = TcState.WAIT_FOR_STATE_TO_BE_SET;

    @Column(name = "type_post")
    private String typePost;

    @Column(name = "type")
    @Convert(converter = SignalConverter.class)
    @JsonDeserialize(using = SignalDeserializer.class)
    private List<Signal> tvOrRadioTypes;

    private Integer govYearComplete;

    public WritableTc() {
    }

    public boolean hasSameEqualsProperties(@Nullable WritableTc lf) {
        if (lf == null) {
            return false;
        }
        return Objects.equals(lf.getGovernmentDevelopmentProgram(), getGovernmentDevelopmentProgram()) &&
                Objects.equals(lf.getOperatorId(), getOperatorId()) &&
                Objects.equals(lf.getTrunkChannel(), getTrunkChannel()) &&
                Objects.equals(lf.getTypeMobile(), getTypeMobile()) &&
                Objects.equals(lf.getType(), getType()) &&
                Objects.equals(lf.getTvOrRadioTypes(), getTvOrRadioTypes()) &&
                Objects.equals(lf.getGovYearComplete(), getGovYearComplete()) &&
                Objects.equals(lf.getQuality(), getQuality()) &&
                Objects.equals(lf.getTypePost(), getTypePost());
    }


    public WritableTc cloneWithNullId() {
        return new WritableTc(
                null,
                getOperatorId(),
                getType(),
                getGovernmentDevelopmentProgram(),
                getTrunkChannel(),
                getTypeMobile(),
                getLocationId(),
                getQuality(),
                getState(),
                getTypePost(),
                getTvOrRadioTypes(),
                getGovYearComplete()
        );
    }

    public boolean isPlan() {
        return governmentDevelopmentProgram != null && govYearComplete >= Year.now().getValue();
    }
}
