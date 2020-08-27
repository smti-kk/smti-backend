package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ApESPD.class, name = "espd"),
        @JsonSubTypes.Type(value = ApSMO.class, name = "smo")
})

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor

@Entity
@Table(name = "access_point")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
//@NamedEntityGraph(name = "AccessPointLocation", attributeNodes = {@NamedAttributeNode(value = "loc")})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = AccessPoint.REPORT_ALL,
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
        )
}
)

//note: https://vladmihalcea.com/postgresql-inet-type-hibernate/
//note: https://github.com/vladmihalcea/hibernate-types/blob/master/hibernate-types-52/src/main/java/com/vladmihalcea/hibernate/type/basic/Inet.java
//@TypeDef(
//        name = "ipv4",
//        typeClass = PostgreSQLInetType.class,
//        defaultForType = Inet.class
//)
public class AccessPoint extends AuditingSoftDelete implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String REPORT_ALL = "AccessPoint.REPORT_ALL";

    @Id
    @SequenceGenerator(name = "ACCESSPOINT_ID_GENERATOR", sequenceName = "accesspoint_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCESSPOINT_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
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

    @Column(columnDefinition = "text")
    private String description;


    /**
     * IP адресс точки подключения.
     * Адресс самой точки. Используется в UTM5 и Zabbix.
     */
    @Column(columnDefinition = "text")
    private String ipConfig;

    @Column
    private Integer maxAmount;

    //TODO: for future thoughts:
    // this information we taking from monitoring system (i.e. UTM5)
//    @Column(name = "net_traffic_last_month")
//    private Long netTrafficLastMonth;

    //TODO: for future thoughts:
    // this information we taking from monitoring system (i.e. UTM5)
//    @Column(name = "net_traffic_last_week")
//    private Long netTrafficLastWeek;

    /**
     * Список сетей, которые распологаются за этой точкой доступа.
     * Эта информация необходима, для билинговой системы UTM5.
     * Шаблон-разделитель "; " - точка с запятой и пробел.
     * А дальше стандартно хх.хх.хх.хх/хх
     */
    @Column(columnDefinition = "text")
    private String networks;

    //TODO: check about this value
    // and if this is good, then make migration for it
    // columnDefinition = "geometry(Point,4326)"
    // from this resource i'v saw https://stackoverflow.com/questions/59291785/error-de-serializing-geometry-with-jackson
    @Column(nullable = false)
    private Point point;

    //hint: normal -
    @Column(length = 6)
    private String quality;

    @Column(length = 4)
    private String state;

    /**
     * UCN  - Unique Contract Number. Уникальынй номер контракта.
     */
    @Column
    private Integer ucn;

    @Column(nullable = false)
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    //bi-directional many-to-one association to CatalogsInternetaccesstype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_internet_access")
    private TypeInternetAccess internetAccess;

    //bi-directional many-to-one association to CatalogsOperator
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_operator")
    private Operator operator;

    //bi-directional many-to-one association to CatalogsOrganization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_organization")
    @JsonIgnore
    private Organization organization;

}