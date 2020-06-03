package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("MOBILE")
public class TcMobile extends TechnicalCapability {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_mobile")
    private TypeMobile type;

    /**
     * Максимальное количество абонентов
     */
    @NotNull
    @PositiveOrZero
    @Column
    private Integer subscribers;
}