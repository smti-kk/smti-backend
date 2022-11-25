package ru.cifrak.telecomit.backend.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data

@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("SMO")
public class ApSMO extends AccessPoint{

    //Дата ввода в эксплуатацию [15]
    @Column(name = "data_commissioning")
    private LocalDateTime dataCommissioning;

}
