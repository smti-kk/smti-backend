package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("INET")
public class TcInternet extends TechnicalCapability {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_trunkchannel")
    private TypeTrunkChannel trunkChannel;

    /**
     * Ширина магистрального канала (Мбит/с)
     */
    @NotNull
    @NotEmpty
    @Column
    private String throughput;

    /**
     * Максимальное количество абонентов
     */
    @NotNull
    @PositiveOrZero
    @Column
    private Integer subscribers;
}