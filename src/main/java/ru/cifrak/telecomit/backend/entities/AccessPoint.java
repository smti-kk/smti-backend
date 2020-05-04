package ru.cifrak.telecomit.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.basic.Inet;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLInetType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the accesspoint database table.
 */
@Data
@Entity
@Table
//note: https://vladmihalcea.com/postgresql-inet-type-hibernate/
//note: https://github.com/vladmihalcea/hibernate-types/blob/master/hibernate-types-52/src/main/java/com/vladmihalcea/hibernate/type/basic/Inet.java
@TypeDef(
        name = "ipv4",
        typeClass = PostgreSQLInetType.class,
        defaultForType = Inet.class
)
public class AccessPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ACCESSPOINT_ID_GENERATOR", sequenceName = "SEQ_ACCESSPOINT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCESSPOINT_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length = 1000)
    private String address;

    @Column
    private Integer billingId;

    private Integer completed;

    @Column(length = 500)
    private String contractor;

    @Column(length = 500)
    private String customer;

    @Column(length = 500)
    private String declaredSpeed;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "inet")
    private Inet ipConfig;

    @Column
    private Integer maxAmount;

    @Column(name = "net_traffic_last_month")
    private Long netTrafficLastMonth;

    @Column(name = "net_traffic_last_week")
    private Long netTrafficLastWeek;

    @Column(length = 500)
    private String node;

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
    @JsonProperty("government_program")
    private GovernmentDevelopmentProgram governmentDevelopmentProgram;

    //bi-directional many-to-one association to CatalogsInternetaccesstype
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_type_internet_access")
    @JsonProperty("connection_type")
    private TypeInternetAccess internetAccess;

    //bi-directional many-to-one association to CatalogsOperator
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_operator")
    @JsonProperty("catalogs_operator")
    private Operator operator;

    //bi-directional many-to-one association to CatalogsOrganization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_organization")
    @JsonBackReference
    private Organization organization;

}