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
@NamedQuery(name = "TypeOrganization.findAll", query = "SELECT c FROM TypeOrganization c")
public class TypeOrganization implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TYPEORGANIZATION_ID_GENERATOR", sequenceName = "typeorganization_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPEORGANIZATION_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String name;

   /* @JsonBackReference
    //bi-directional many-to-one association to Organization
    @OneToMany(mappedBy= "type")
    private List<Organization> catalogsOrganizations;

    @JsonIgnore
    //bi-directional many-to-one association to OrganizationAvailableOrganizationType
    @OneToMany(mappedBy="catalogsOrganizationtype")
    private List<OrganizationAvailableOrganizationType> catalogsOrganizationAvailableOrganizationTypes;*/
}
