package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("ATS")
public class TcAts extends TechnicalCapability {
    /**
     * Количество ТАКСОФОНОВ
     */
    @NotNull
    @PositiveOrZero
    @Column
    private Integer payphones;

    /**
     * Монтируемая ёмкость
     */
    @NotNull
    @PositiveOrZero
	@Column
	private Integer capacity;

    /**
     * Максимальное количество абонентов
     */
    @PositiveOrZero
    @Column
    private Integer subscribers;
}