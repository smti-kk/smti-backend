package ru.cifrak.telecomit.backend.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Data

@Entity
@DiscriminatorValue("CONTRACT")
public class ApContract extends AccessPoint{
    @Column
    private String number;
    //TODO: ask Lev about contract
    @Column
    private Long amount;
    @Column
    private LocalDate started;
    @Column
    private LocalDate ended;
}
