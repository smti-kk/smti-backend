package ru.cifrak.telecomit.backend.entities;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data

@Entity
@DiscriminatorValue("SMO")
public class ApESPD extends AccessPoint{
}
