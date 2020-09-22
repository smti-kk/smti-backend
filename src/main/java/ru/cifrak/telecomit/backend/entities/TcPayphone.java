package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("PAYPHONE")
public class TcPayphone extends TechnicalCapability{
    /**
     * Количество Payphone (телефон - автомат)
     */
    @PositiveOrZero
    @Column
    private Integer quantity;
}
