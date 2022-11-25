package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Data

@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("ESPD")
public class ApESPD extends AccessPoint{

    //Белый IP ЕСПД [16]
    private String espdWhiteIp;

    //№ вх.письма от ведомства [20]
    private String numSourceEmailsRTK;

    //Разовый, руб. с НДС [23]
    private BigDecimal oneTimePay;

    //Ежемес, руб. с НДС [24]
    private BigDecimal mounthlyPay;

    //Белый IP ЗСПД [26]
    private String zspdWhiteIp;

    // наличие ЗСПД/ способ подключения к ЗСПД [27]
    private String availZspdOrMethodConToZspd;

}
