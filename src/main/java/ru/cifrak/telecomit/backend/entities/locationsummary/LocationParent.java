package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Getter;
import ru.cifrak.telecomit.backend.entities.Location;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "location")
@Getter
public class LocationParent implements Serializable {
    @Id
    private Integer id;
    private String type;
    private String name;
    private Integer level;

    public LocationParent(Location location) {
        this.id = location.getId();
        this.type = location.getType();
        this.name = location.getName();
        this.level = location.getLevel();
    }

    public LocationParent(Integer id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public LocationParent() {
    }
}
