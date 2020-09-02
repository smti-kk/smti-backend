package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("TV")
public class TcTv extends TechnicalCapability {
    @NotNull
    @Column
    @Convert(converter = SignalConverter.class)
    private List<Signal> type;

    /**
     * Число каналов цифрового ТВ
     */
    @NotNull
    @PositiveOrZero
    @Column
    private Integer dChannels;

    /**
     * Число каналов аналогового ТВ
     */
    @NotNull
    @PositiveOrZero
    @Column
    private Integer aChannels;

    @JsonIgnore
    public String signalAsString() {
        return type.stream()
                .map(Signal::getName)
                .collect(Collectors.joining(", "));
    }

}
