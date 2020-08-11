package ru.cifrak.telecomit.backend.base.station.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.Operator;
import ru.cifrak.telecomit.backend.entities.TypeMobile;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.*;
import java.util.Date;

/**
 * Базовые станции
 */
@NamedEntityGraphs(
        @NamedEntityGraph(
                name = "base_station_full",
                attributeNodes = {
                        @NamedAttributeNode(value = "operator"),
                        @NamedAttributeNode(value = "mobileType")
                }
        )
)
@Entity
@Data
public class BaseStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Integer id;
    private final String address;
    private final Double propHeight;

    @CreationTimestamp
    private final Date actionDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private final Operator operator;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private final TypeMobile mobileType;

    @Column(nullable = false)
    private final Double coverageRadius;

    @Column(nullable = false)
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private final Point point;

    public BaseStation(Integer id,
                       String address,
                       Double propHeight,
                       Date actionDate,
                       Operator operator,
                       TypeMobile mobileType,
                       Double coverageRadius,
                       Point point) {
        this.id = id;
        this.address = address;
        this.propHeight = propHeight;
        this.actionDate = actionDate;
        this.operator = operator;
        this.mobileType = mobileType;
        this.coverageRadius = coverageRadius;
        this.point = point;
    }

    /**
     * Hibernate requires that constructor
     */
    public BaseStation() {
        mobileType = null;
        operator = null;
        actionDate = null;
        propHeight = null;
        address = null;
        id = null;
        coverageRadius = null;
        point = null;
    }
}
