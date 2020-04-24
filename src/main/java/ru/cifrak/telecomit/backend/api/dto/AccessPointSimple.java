package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.domain.CatalogsGovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.domain.MonitoringAccesspointRe;
import ru.cifrak.telecomit.backend.domain.MonitoringConnectionMetric;
import ru.cifrak.telecomit.backend.serializer.ConnectionMetricSerializer;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import java.util.Comparator;

@Data
@AllArgsConstructor
public class AccessPointSimple {
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

    @JsonSerialize(using = ConnectionMetricSerializer.class)
    private MonitoringConnectionMetric avstatus;

    @JsonProperty(value = "government_program")
    private CatalogsGovernmentDevelopmentProgram governmentProgram;

    public AccessPointSimple(MonitoringAccesspointRe entity) {
        this.id = entity.getId();
        this.point = entity.getPoint();

        this.governmentProgram = new CatalogsGovernmentDevelopmentProgram(
                entity.getCatalogsGovernmentDevelopmentProgram().getId(),
                entity.getCatalogsGovernmentDevelopmentProgram().getDescription(),
                entity.getCatalogsGovernmentDevelopmentProgram().getFullName(),
                entity.getCatalogsGovernmentDevelopmentProgram().getShortName()
        );

        this.avstatus = entity.getMonitoringConnectionMetrics().stream()
                .min(Comparator.comparingLong((mcm) -> mcm.getTimeMark().getTime()))
                .orElse(null);
    }
}
