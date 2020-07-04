package ru.cifrak.telecomit.backend.entities.map;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "technical_capability")
@Getter
public class ShortTechnicalCapability {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;

    public ShortTechnicalCapability() {
    }
}
