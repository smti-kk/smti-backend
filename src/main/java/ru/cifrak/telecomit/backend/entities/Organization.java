package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table
@NamedQuery(name = "Organization.findAll", query = "SELECT c FROM Organization c")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = Organization.FULL,
                attributeNodes = {
                        @NamedAttributeNode("location"),
                        @NamedAttributeNode("type"),
                        @NamedAttributeNode("smo"),
                        @NamedAttributeNode(value = "accessPoints", subgraph = "accesspoints"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "accesspoints",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "monitoringLink", subgraph = "jmap"),
                                        @NamedAttributeNode("internetAccess"),
                                        @NamedAttributeNode("governmentDevelopmentProgram"),
                                }
                        ),
                        @NamedSubgraph(
                                name = "jmap",
                                attributeNodes = {
                                        @NamedAttributeNode("map"),
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = Organization.REPORT_AP_ALL,
                attributeNodes = {
                        @NamedAttributeNode("location"),
                        @NamedAttributeNode("type"),
                        @NamedAttributeNode("smo"),
                        @NamedAttributeNode(value = "accessPoints", subgraph = "aps"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "aps",
                                attributeNodes = {
                                        @NamedAttributeNode("governmentDevelopmentProgram"),
                                        @NamedAttributeNode("internetAccess"),
                                        @NamedAttributeNode("operator"),
                                }
                        )
                }
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization implements Serializable {
    public static final String FULL = "Organization.FULL";
    public static final String REPORT_AP_ALL = "Organization.REPORT_AP_ALL";
    public static final String REPORT_AP_CONTRACT = "Organization.REPORT_AP_CONTRACT";
    private static final long serialVersionUID = 1L;

    @ToString.Include
    @Id
    @SequenceGenerator(name = "ORGANIZATION_ID_GENERATOR", sequenceName = "organization_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGANIZATION_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @ToString.Include
    @Column(nullable = false)
    private UUID fias;

    @ToString.Include
    @Column(nullable = false, columnDefinition = "text")
    private String name;

    @Column(nullable = false, length = 12)
    private String inn;

    @Column(nullable = false, length = 9)
    private String kpp;

    /**
     * Better variable naming for short naming jf organization
     */
    @ToString.Include
    @Column(nullable = false, length = 500)
    private String acronym;

    //bi-directional many-to-one association to Location
    @JsonIgnoreProperties({"location", "parent", "geoData", "children"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_location", nullable = false)
    private Location location;

    //bi-directional many-to-one association to Organization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_parent")
    private Organization parent;

    //bi-directional many-to-one association to Organization
    @JsonIgnoreProperties("parent")
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

    @JsonIgnoreProperties("organization")
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<AccessPoint> accessPoints;

    @Column
    private Boolean main;

    // Функциональный заказчик
    @ManyToOne
    @JoinColumn(name = "key_fun_customer")
    private FunCustomer funCustomer;
}