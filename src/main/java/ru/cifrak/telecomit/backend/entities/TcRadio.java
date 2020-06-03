package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("RADIO")
public class TcRadio extends TechnicalCapability {
    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private Signal type;
}