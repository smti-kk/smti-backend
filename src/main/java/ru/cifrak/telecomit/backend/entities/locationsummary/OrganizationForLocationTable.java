package ru.cifrak.telecomit.backend.entities.locationsummary;

import lombok.Getter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity()
@Table(name = "organization")
@Getter
public class OrganizationForLocationTable implements Serializable {

    @Id
    private Integer id;

    @Column(name = "key_location")
    private Integer locationId;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "key_organization")
    private Set<AccessPointForTable> accessPoints;
}
