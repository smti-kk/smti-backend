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

    @OneToMany
    @JoinColumn(name = "key_organization")
    private Set<AccessPointForTable> accessPoints;
}
