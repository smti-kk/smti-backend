package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("POST")
public class TcPost extends TechnicalCapability {
    @NotNull
    @Column(name = "type_post")
    @Enumerated(EnumType.STRING)
    private TypePost type;
}