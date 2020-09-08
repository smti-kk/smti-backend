package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("RADIO")
public class TcRadio extends TechnicalCapability {
    @NotNull
    @Column
//    @Enumerated(EnumType.STRING)
    @Convert(converter = SignalConverter.class)
    private List<Signal> type;

    @JsonIgnore
    public String signalAsString() {
        return type.stream()
                .map(Signal::getName)
                .collect(Collectors.joining(", "));
    }

}