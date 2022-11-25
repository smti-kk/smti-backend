package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccessPointFull {
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

//    @JsonSerialize(using = ConnectionMetricSerializer.class)
//    private MonitoringConnectionMetric avstatus;

    @JsonProperty(value = "government_program")
    private GovernmentDevelopmentProgram governmentProgram;

    private String address;
    @JsonProperty(value = "billing_id")
    private Integer billingId;
    private Integer completed;
    private String contractor;
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;
    private String customer;
    @JsonProperty(value = "defined_speed")
    private String definedSpeed;
    private String description;
    @JsonProperty(value = "ip_config")
    private String ipConfig;
    @JsonProperty(value = "max_amount")
    private Integer maxAmount;
    private String name;
    @JsonProperty(value = "net_traffic_last_month")
    private Long netTrafficLastMonth;
    @JsonProperty(value = "net_traffic_last_week")
    private Long netTrafficLastWeek;
    private String networks;
    private String quality;
    private String state;
    private Integer ucn;
    @JsonProperty(value = "updated_at")
    private LocalDateTime updatedAt;
    private Boolean visible;

    private Operator operator;
    @JsonProperty(value = "connection_type")
    private TypeInternetAccess connectionType;
    private String equipment;
    private String softType;


    public AccessPointFull(AccessPoint entity) {
        this.id = entity.getId();
        this.point = entity.getPoint();
        this.address = entity.getAddress();
        this.billingId = entity.getBillingId();
        this.completed = entity.getCompleted();
        //this.contractor = entity.getContractor();
        this.createdAt = entity.getCreatedDate();
        this.customer = entity.getCustomer();
        this.definedSpeed = entity.getDeclaredSpeed();
        this.description = entity.getDescription();
        this.ipConfig = entity.getIpConfig().toString();
        this.maxAmount = entity.getMaxAmount();
//        this.netTrafficLastMonth = entity.getNetTrafficLastMonth();
//        this.netTrafficLastWeek = entity.getNetTrafficLastWeek();
        this.networks = entity.getNetworks();
        this.quality = entity.getQuality();
        this.state = entity.getState() != null ? entity.getState().toString() : null;
        this.ucn = entity.getUcn();
        this.updatedAt = entity.getModifiedDate();
        this.visible = entity.getVisible();

        /*this.governmentProgram = new GovernmentDevelopmentProgram(
                entity.getGovernmentDevelopmentProgram().getId(),
                entity.getGovernmentDevelopmentProgram().getDescription(),
                entity.getGovernmentDevelopmentProgram().getName(),
                entity.getGovernmentDevelopmentProgram().getAcronym()
        );*/

//        this.avstatus = entity.getMonitoringConnectionMetrics().stream()
//                .min(Comparator.comparingLong((mcm) -> mcm.getTimeMark().getTime()))
//                .orElse(null);

        this.operator = entity.getOperator();
        this.connectionType = entity.getInternetAccess();
        if (entity.getClass().isAssignableFrom(ApEMSPD.class)) {
            this.equipment = ((ApEMSPD) entity).getEquipment();
            this.softType = ((ApEMSPD) entity).getSoftType();
        }
    }
}
