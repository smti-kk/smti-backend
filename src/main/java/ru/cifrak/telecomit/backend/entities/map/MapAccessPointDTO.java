package ru.cifrak.telecomit.backend.entities.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import ru.cifrak.telecomit.backend.entities.APConnectionState;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;
import java.util.Optional;

@Data
public class MapAccessPointDTO {
    @Id
    private Integer id;

    @JsonProperty("point")
    @JsonSerialize(using = GeometrySerializer.class)
    private Geometry point;

    @JsonIgnore
    private String type;

    @JsonIgnore
    private Date modified;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private APConnectionState connectionState;

    public MapAccessPointDTO(Integer id, Geometry point, APConnectionState connectionState) {
        this.id = id;
        this.point = point;
        if (connectionState != null) {
            this.connectionState = connectionState;
        } else {
            this.connectionState = APConnectionState.NOT_MONITORED;
        }
    }

    public MapAccessPointDTO(AccessPointFull item) {
        this.id = item.getId();
        this.point = item.getPoint();
        this.connectionState = Optional.ofNullable(item.getMonitoringLink())
                .map(JournalMAP::getMap)
                .map(MonitoringAccessPoint::getConnectionState)
                .orElse(APConnectionState.NOT_MONITORED);
    }

    public MapAccessPointDTO() {
    }

    public Integer getId() {
        return id;
    }

    public Geometry getPoint() {
        return point;
    }
}
