package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;
import ru.cifrak.telecomit.backend.serializer.SignalOneConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("RADIO")
public class TcRadio extends TechnicalCapability {
    @NotNull
    @Column
//    @Enumerated(EnumType.STRING)
    @Convert(converter = SignalOneConverter.class)
    private Signal type;
}