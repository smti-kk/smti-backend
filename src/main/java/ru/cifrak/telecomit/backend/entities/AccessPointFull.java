package ru.cifrak.telecomit.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor

@Entity
@Table(name = "access_point")

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = AccessPointFull.REPORT_ALL,
                attributeNodes = {
                        @NamedAttributeNode("governmentDevelopmentProgram"),
                        @NamedAttributeNode("internetAccess"),
                        @NamedAttributeNode("operator"),
                        @NamedAttributeNode(value = "organization", subgraph = "org-loc"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "org-loc",
                                attributeNodes = {
                                        @NamedAttributeNode("location"),
                                        @NamedAttributeNode("type"),
                                        @NamedAttributeNode("smo"),
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = AccessPointFull.REPORT_ALL_EXPORT,
                attributeNodes = {
                        @NamedAttributeNode("governmentDevelopmentProgram"),
                        @NamedAttributeNode("internetAccess"),
                        @NamedAttributeNode("operator"),
                        @NamedAttributeNode(value = "organization", subgraph = "org-loc-1"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "org-loc-1",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "location", subgraph = "org-loc-parent"),
                                        @NamedAttributeNode("type"),
                                        @NamedAttributeNode("smo"),
                                }
                        ),
                        @NamedSubgraph(
                                name = "org-loc-parent",
                                attributeNodes = {
                                        @NamedAttributeNode("parent")
                                }
                        )
                }
        )
}
)

@Immutable
public class AccessPointFull extends AuditingSoftDelete implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String REPORT_ALL = "AccessPointFull.REPORT_ALL";
    public static final String REPORT_ALL_EXPORT = "AccessPointFull.REPORT_ALL_EXPORT";

    @Id
    private Integer id;

    @Column(length = 1000)
    private String address;

    @Column
    private Integer billingId;

    /**
     * Год когда закончилась гос.программа.
     * <br/>Нам интересен только год, месяц и день не интересно.
     */
    @Column
    private Integer completed;

    @Column(length = 500)
    private String contractor;

    @Column(length = 500)
    private String customer;

    @Column(length = 500)
    private String declaredSpeed;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeAccessPoint type;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String ipConfig;

    @Column
    private Integer maxAmount;

    @Column(name = "net_traffic_last_month")
    private Long netTrafficLastMonth;

    @Column(name = "net_traffic_last_week")
    private Long netTrafficLastWeek;

    @Column(columnDefinition = "text")
    private String networks;

    //    @JsonSerialize(using = GeometrySerializer.class)
//    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(nullable = false)
    private Point point;

    //hint: normal -
    @Column(length = 6)
    private String quality;

    @Column(length = 4)
    private String state;

    private Integer ucn;

    @Column(nullable = false)
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    //bi-directional many-to-one association to CatalogsInternetaccesstype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_internet_access", nullable = false)
    private TypeInternetAccess internetAccess;

    //bi-directional many-to-one association to CatalogsOperator
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_operator")
    private Operator operator;

    //bi-directional many-to-one association to CatalogsOrganization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_organization")
    private Organization organization;

    // FIELDS FOR CONTRACT TYPE
    @Column
    private String number;
    @Column
    private Long amount;
    @Column
    private LocalDate started;
    @Column
    private LocalDate ended;


    @OneToOne(mappedBy = "ap")
    private JournalMAP monitoringLink;

}