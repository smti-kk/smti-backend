package ru.cifrak.telecomit.backend.repository.dto;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

/**
 * Упрощенная entity для вывода информации о локации на карте
 */
@Getter
@Entity
@Table(name = "location")
public class ShortLocation {
    @Id
    private Integer id;
    private Integer population;
    private String type;
    private String name;

    @OneToMany()
    @JoinColumn(name = "key_location")
    private List<ShortTechnicalCapability> technicalCapability;

    public ShortLocation() {
    }
}

