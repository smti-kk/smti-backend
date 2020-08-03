package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;
import ru.cifrak.telecomit.backend.serializer.SignalOneConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("RADIO")
public class TcRadio extends TechnicalCapability {
    @NotNull
    @Column
//    @Enumerated(EnumType.STRING)
    @Convert(converter = SignalOneConverter.class)
    private List<Signal> type;
}