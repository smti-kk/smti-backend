package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;



@Data
@NoArgsConstructor

@Entity
@Table
@NamedQuery(name = "Organization.findAll", query = "SELECT c FROM Organization c")
/*@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Organization.FULL,
                attributeNodes = {
                        @NamedAttributeNode(value = "monitoringAccesspointRes"*//*, subgraph = "accesspoints"*//*),
                        @NamedAttributeNode("type"),
                        @NamedAttributeNode("smoType"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "accesspoints",
                                attributeNodes = {
                                    @NamedAttributeNode("GovernmentDevelopmentProgram"),
                                    @NamedAttributeNode("Internetaccesstype"),
                                    @NamedAttributeNode("Operator"),
                                    @NamedAttributeNode("Contract"),
                                }
                        )
                }
        )
})*/
public class Organization implements Serializable {
    public static final String FULL = "Organization.FULL";
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ORGANIZATION_ID_GENERATOR", sequenceName = "organization_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGANIZATION_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(nullable = false)
    private UUID fias;

    @Column(nullable = false, columnDefinition = "text")
    private String name;

    @Column(nullable = false, length = 12)
    private String inn;

    @Column(nullable = false, length = 9)
    private String kpp;

    /**
     * Better variable naming for short naming jf organization
     */
    @Column(nullable = false, length = 500)
    private String acronym;

    //bi-directional many-to-one association to Location
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location", nullable = false)
    private Location location;

    //bi-directional many-to-one association to Organization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_parent")
    private Organization parent;

    //bi-directional many-to-one association to Organization
    @OneToMany(mappedBy = "parent")
    private List<Organization> children;

    //bi-directional many-to-one association to Organizationtype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type")
    private TypeOrganization type;

    //bi-directional many-to-one association to Smotype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_smo")
    private TypeSmo smo;

}