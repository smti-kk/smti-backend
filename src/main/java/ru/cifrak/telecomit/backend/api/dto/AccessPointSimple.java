package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.GovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

@Data
@AllArgsConstructor
public class AccessPointSimple {
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

//    @JsonSerialize(using = ConnectionMetricSerializer.class)
//    private MonitoringConnectionMetric avstatus;

    @JsonProperty(value = "government_program")
    private GovernmentDevelopmentProgram governmentProgram;

    public AccessPointSimple(AccessPoint entity) {
        this.id = entity.getId();
        this.point = entity.getPoint();

//        this.governmentProgram = new GovernmentDevelopmentProgram(
//                entity.getGovernmentDevelopmentProgram().getId(),
//                entity.getGovernmentDevelopmentProgram().getDescription(),
//                entity.getGovernmentDevelopmentProgram().getName(),
//                entity.getGovernmentDevelopmentProgram().getAcronym()
//        );

//        this.avstatus = entity.getMonitoringConnectionMetrics().stream()
//                .min(Comparator.comparingLong((mcm) -> mcm.getTimeMark().getTime()))
//                .orElse(null);
    }
}
