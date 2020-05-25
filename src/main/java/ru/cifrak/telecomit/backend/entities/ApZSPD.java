package ru.cifrak.telecomit.backend.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data

@Entity
@DiscriminatorValue("ZSPD")
public class ApZSPD extends AccessPoint{
    @Column
    private Hardware hardware;
    @Column
    private Software software;
}
