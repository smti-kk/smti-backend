package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("ATS")
public class TcAts extends TechnicalCapability {


}