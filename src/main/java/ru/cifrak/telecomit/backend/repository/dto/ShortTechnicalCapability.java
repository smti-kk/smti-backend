package ru.cifrak.telecomit.backend.repository.dto;


import javax.persistence.*;

@Entity
@Table(name = "technical_capability")
public class ShortTechnicalCapability {
    @Id
    private Long id;

    @Column(name = "key_operator")
    private Integer operatorId;

    @Column(name = "split")
    private String type;
}
