package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private TypeMobile type;
}