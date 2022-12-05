package ru.cifrak.telecomit.backend.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("SMO")
public class ApSMO extends AccessPoint {

    // Дата ввода в эксплуатацию
    @Column(name = "date_commissioning")
    private LocalDate dateCommissioning;

}
