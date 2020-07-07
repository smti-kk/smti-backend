package ru.cifrak.telecomit.backend.entities.map;

import lombok.Getter;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.List;

/**
 * Упрощенная entity для вывода информации о локации на карте
 */
@Getter
@Entity
@Immutable
@Table(name = "location")
public class ShortLocation {
    @Id
    private Integer id;
    private Integer population;
    private String type;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_location")
    private List<ShortTechnicalCapability> technicalCapability;


    public ShortLocation() {
    }
}

