package ru.cifrak.telecomit.backend.api.dto;

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
public class OrganizationMoreAccessPointDTO {
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

    private GovernmentDevelopmentProgram governmentProgram;

    private String address;
    private Integer billingId;
    private Integer completed;
    private String contractor;
    private LocalDateTime createdAt;
    private String customer;
    private String declaredSpeed;
    private String description;
    private String ipConfig;
    private Integer maxAmount;
    private String name;
    private Long netTrafficLastMonth;
    private Long netTrafficLastWeek;
    private String node;
    private String quality;
    private String state;
    private Integer ucn;
    private LocalDateTime updatedAt;
    private Boolean visible;

    private OperatorSimpleDTO operator;
    private TypeInternetAccessDTO internetAccess;
    private String type;


    public OrganizationMoreAccessPointDTO(AccessPoint entity) {
        this.id = entity.getId();
        this.point = entity.getPoint();
        this.address = entity.getAddress();
        this.billingId = entity.getBillingId();
        this.completed = entity.getCompleted();
        this.contractor = entity.getContractor();
        this.createdAt = entity.getCreatedDate();
        this.customer = entity.getCustomer();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.description = entity.getDescription();
        this.ipConfig = entity.getIpConfig();
        this.maxAmount = entity.getMaxAmount();
        this.netTrafficLastMonth = entity.getNetTrafficLastMonth();
        this.netTrafficLastWeek = entity.getNetTrafficLastWeek();
        this.node = entity.getNode();
        this.quality = entity.getQuality();
        this.state = entity.getState();
        this.ucn = entity.getUcn();
        this.updatedAt = entity.getModifiedDate();
        this.visible = entity.getVisible();

        if (entity.getGovernmentDevelopmentProgram() != null) {
            this.governmentProgram = new GovernmentDevelopmentProgram(
                    entity.getGovernmentDevelopmentProgram().getId(),
                    entity.getGovernmentDevelopmentProgram().getDescription(),
                    entity.getGovernmentDevelopmentProgram().getName(),
                    entity.getGovernmentDevelopmentProgram().getAcronym()
            );
        }

        this.operator = entity.getOperator() != null ? new OperatorSimpleDTO(entity.getOperator()) : null;
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        if (entity.getClass().isAssignableFrom(ApSMO.class)) {
            this.type = "SMO";
        } else if (entity.getClass().isAssignableFrom(ApESPD.class)) {
            this.type = "ESPD";
        } else if (entity.getClass().isAssignableFrom(ApZSPD.class)) {
            this.type = "ZSPD";
        } else if (entity.getClass().isAssignableFrom(ApRSMO.class)) {
            this.type = "RSMO";
        } else if (entity.getClass().isAssignableFrom(ApContract.class)) {
            this.type = "CONTRACT";
        }
    }
}
