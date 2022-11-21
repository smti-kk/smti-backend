package ru.cifrak.telecomit.backend.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data

@Entity
@DiscriminatorValue("EMSPD")
public class ApEMSPD extends AccessPoint{
    @Column
    private Hardware hardware;
    @Column
    private Software software;
    @Column
    private String equipment;
    @Column
    private String softType;
}
