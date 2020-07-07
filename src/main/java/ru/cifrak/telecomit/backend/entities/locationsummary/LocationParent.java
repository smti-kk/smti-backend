package ru.cifrak.telecomit.backend.entities.locationsummary;

import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "location")
@Immutable
public class LocationParent {
    @Id
    private Integer id;
    private String type;
    private String name;
}
