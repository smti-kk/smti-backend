package ru.cifrak.telecomit.backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the catalogs_organization database table.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "catalogs_organization")
@NamedQuery(name = "CatalogsOrganization.findAll", query = "SELECT c FROM CatalogsOrganization c")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = CatalogsOrganization.FULL,
                attributeNodes = {
                        @NamedAttributeNode(value = "monitoringAccesspointRes", subgraph = "accesspoints"),
                        @NamedAttributeNode("type"),
                        @NamedAttributeNode("smoType"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "accesspoints",
                                attributeNodes = {
                                    @NamedAttributeNode("catalogsGovernmentDevelopmentProgram"),
                                    @NamedAttributeNode("catalogsInternetaccesstype"),
                                    @NamedAttributeNode("catalogsOperator"),
                                    @NamedAttributeNode("catalogsContract"),
                                }
                        )
                }
        )
})
public class CatalogsOrganization implements Serializable {
    public static final String FULL = "CatalogsOrganization.FULL";
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CATALOGS_ORGANIZATION_ID_GENERATOR", sequenceName = "CATALOGS_ORGANIZATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOGS_ORGANIZATION_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 2147483647)
    private String address;

    @Column(nullable = false)
    private UUID fias;

    @Column(name = "full_name", nullable = false, length = 2147483647)
    private String fullName;

    @Column(nullable = false)
    private Integer inn;

    @Column(nullable = false)
    private Integer kpp;

    @Column(nullable = false, length = 500)
    private String name;

    //bi-directional many-to-one association to CatalogsContract
    @OneToMany(mappedBy = "catalogsOrganization")
    private List<CatalogsContract> catalogsContracts;

	/*//bi-directional many-to-one association to CatalogsNetworkconnection
	@OneToMany(mappedBy="catalogsOrganization")
	private List<CatalogsNetworkconnection> catalogsNetworkconnections;*/

    //bi-directional many-to-one association to CatalogsLocation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private CatalogsLocation catalogsLocation;

    //bi-directional many-to-one association to CatalogsOrganization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CatalogsOrganization parent;

    //bi-directional many-to-one association to CatalogsOrganization
    @OneToMany(mappedBy = "parent")
    private List<CatalogsOrganization> catalogsOrganizations;

    //bi-directional many-to-one association to CatalogsOrganizationtype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private CatalogsOrganizationtype type;

    //bi-directional many-to-one association to CatalogsSmotype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_smo_id")
    private CatalogsSmotype smoType;

    //bi-directional many-to-one association to CatalogsOrganizationAvailableOrganizationType
    @OneToMany(mappedBy = "catalogsOrganization")
    private List<CatalogsOrganizationAvailableOrganizationType> catalogsOrganizationAvailableOrganizationTypes;

    //bi-directional many-to-one association to CatalogsOrganizationAvailableSmoType
    @OneToMany(mappedBy = "catalogsOrganization")
    private List<CatalogsOrganizationAvailableSmoType> catalogsOrganizationAvailableSmoTypes;

    //bi-directional many-to-one association to MonitoringAccesspointRe
    @OneToMany(mappedBy = "catalogsOrganization")
    private List<MonitoringAccesspointRe> monitoringAccesspointRes;
}