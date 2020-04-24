package ru.cifrak.telecomit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import ru.cifrak.telecomit.backend.domain.CatalogsContract;
import ru.cifrak.telecomit.backend.domain.CatalogsGovernmentDevelopmentProgram;
import ru.cifrak.telecomit.backend.domain.CatalogsInternetaccesstype;
import ru.cifrak.telecomit.backend.domain.CatalogsOperator;
import ru.cifrak.telecomit.backend.domain.MonitoringAccesspointRe;
import ru.cifrak.telecomit.backend.domain.MonitoringConnectionMetric;
import ru.cifrak.telecomit.backend.serializer.ConnectionMetricSerializer;
import ru.cifrak.telecomit.backend.serializer.GeometryDeserializer;
import ru.cifrak.telecomit.backend.serializer.GeometrySerializer;

import java.sql.Timestamp;
import java.util.Comparator;

@Data
@AllArgsConstructor
public class AccessPointFull {
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

    @JsonSerialize(using = ConnectionMetricSerializer.class)
    private MonitoringConnectionMetric avstatus;

    @JsonProperty(value = "government_program")
    private CatalogsGovernmentDevelopmentProgram governmentProgram;

    private String address;
    @JsonProperty(value = "billing_id")
    private Integer billingId;
    private Integer completed;
    private String contractor;
    @JsonProperty(value = "created_at")
    private Timestamp createdAt;
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
    private String node;
    private String quality;
    private String state;
    private Integer ucn;
    @JsonProperty(value = "updated_at")
    private Timestamp updatedAt;
    private Boolean visible;

    private CatalogsOperator operator;
    @JsonProperty(value = "connection_type")
    private CatalogsInternetaccesstype connectionType;
    private CatalogsContract contract;


    public AccessPointFull(MonitoringAccesspointRe entity) {
        this.id = entity.getId();
        this.point = entity.getPoint();
        this.address = entity.getAddress();
        this.billingId = entity.getBillingId();
        this.completed = entity.getCompleted();
        this.contractor = entity.getContractor();
        this.createdAt = entity.getCreatedAt();
        this.customer = entity.getCustomer();
        this.definedSpeed = entity.getDefinedSpeed();
        this.description = entity.getDescription();
        this.ipConfig = entity.getIpConfig();
        this.maxAmount = entity.getMaxAmount();
        this.name = entity.getName();
        this.netTrafficLastMonth = entity.getNetTrafficLastMonth();
        this.netTrafficLastWeek = entity.getNetTrafficLastWeek();
        this.node = entity.getNode();
        this.quality = entity.getQuality();
        this.state = entity.getState();
        this.ucn = entity.getUcn();
        this.updatedAt = entity.getUpdatedAt();
        this.visible = entity.getVisible();

        this.governmentProgram = new CatalogsGovernmentDevelopmentProgram(
                entity.getCatalogsGovernmentDevelopmentProgram().getId(),
                entity.getCatalogsGovernmentDevelopmentProgram().getDescription(),
                entity.getCatalogsGovernmentDevelopmentProgram().getFullName(),
                entity.getCatalogsGovernmentDevelopmentProgram().getShortName()
        );

        this.avstatus = entity.getMonitoringConnectionMetrics().stream()
                .min(Comparator.comparingLong((mcm) -> mcm.getTimeMark().getTime()))
                .orElse(null);

        this.operator = entity.getCatalogsOperator();
        this.connectionType = entity.getCatalogsInternetaccesstype();
        this.contract = entity.getCatalogsContract();
    }
}
