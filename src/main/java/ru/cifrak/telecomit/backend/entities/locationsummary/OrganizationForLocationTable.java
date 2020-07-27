package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity()
@Table(name = "organization")
@Getter
public class OrganizationForLocationTable {

    @Id
    private Integer id;

    @Column(name = "key_location")
    private Integer locationId;

    @OneToMany
    @JoinColumn(name = "key_organization")
    private Set<AccessPointForTable> accessPoints;
}
