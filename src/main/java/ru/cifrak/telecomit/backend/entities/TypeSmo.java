package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor

@Entity
@Table
@NamedQuery(name = "TypeSmo.findAll", query = "SELECT c FROM TypeSmo c")
public class TypeSmo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TYPESMO_ID_GENERATOR", sequenceName = "typesmo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPESMO_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String name;
/*
    //bi-directional many-to-one association to Organization
    @OneToMany(mappedBy= "smoType")
    private List<Organization> catalogsOrganizations;

    //bi-directional many-to-one association to OrganizationAvailableSmoType
    @OneToMany(mappedBy="catalogsSmotype")
    private List<OrganizationAvailableSmoType> catalogsOrganizationAvailableSmoTypes;
    */

}
