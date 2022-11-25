package ru.cifrak.telecomit.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
    @Column(name = "espd_white_ip")
    private String espdWhiteIp;

    //№ вх.письма от ведомства [20]
    @Column(name = "num_source_emails_RTK")
    private String numSourceEmailsRTK;

    //Разовый, руб. с НДС [23]
    @Column(name = "one_time_pay")
    private BigDecimal oneTimePay;

    //Ежемес, руб. с НДС [24]
    @Column(name = "monthly_pay")
    private BigDecimal mounthlyPay;

    //Белый IP ЗСПД [26]
    @Column(name = "zspd_white_ip")
    private String zspdWhiteIp;

    // наличие ЗСПД/ способ подключения к ЗСПД [27]
    @Column(name = "avail_zspd_or_method_con_to_zspd")
    private String availZspdOrMethodConToZspd;

}
