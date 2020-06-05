package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cifrak.telecomit.backend.serializer.SignalConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


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

    /**
     * Максимальное количество абонентов
     */
    @NotNull
    @PositiveOrZero
    @Column
    private Integer subscribers;
}
