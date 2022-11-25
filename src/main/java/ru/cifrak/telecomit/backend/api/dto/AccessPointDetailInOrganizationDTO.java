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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>DTO</b> <i>Access Point</i> with all common properties, except ORGANIZATION.
 * <br/>With all subtypes for each Type.
 */
@Data
@AllArgsConstructor
public class AccessPointDetailInOrganizationDTO {
    // COMMON FIELDS
    private Integer id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

    @JsonProperty("government_program")
    private GovernmentDevelopmentProgram governmentProgram;
    private Integer completed;
    private ParticipationStatus state;

    private String address;
    private Integer billingId;
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
    private String networks;
    private String quality;
    private Integer ucn;
    private LocalDateTime updatedAt;
    private Boolean visible;
    private OperatorSimpleDTO operator;
    private TypeInternetAccessDTO internetAccess;
    private String type;

    // SMO
    // -- NA
    // ESPD
    // -- NA
    // RSMO
    // -- NA
    // ZSPD
    private String hardware;
    private String software;
    // CONTRACT
    private String number;
    private Long amount;
    private LocalDate started;
    private LocalDate ended;
    private String equipment;
    private String softType;

    //MONITORING STATUSES
    private Boolean utm5;
    private Boolean zabbix;

    public AccessPointDetailInOrganizationDTO(AccessPoint entity) {
        // COMMON FIELDS
        this.id = entity.getId();
        this.point = entity.getPoint();
        this.address = entity.getAddress();
        this.billingId = entity.getBillingId();
        this.completed = entity.getCompleted();
        this.contractor = null;
        this.createdAt = entity.getCreatedDate();
        this.customer = entity.getCustomer();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.description = entity.getDescription();
        this.ipConfig = entity.getIpConfig();
        this.maxAmount = entity.getMaxAmount();
//        this.netTrafficLastMonth = entity.getNetTrafficLastMonth();
//        this.netTrafficLastWeek = entity.getNetTrafficLastWeek();
        this.networks = entity.getNetworks();
        this.quality = entity.getQuality();
        this.state = entity.getState();// != null ? entity.getState().toString() : null;
        this.ucn = entity.getUcn();
        this.updatedAt = entity.getModifiedDate();
        this.visible = entity.getVisible();

        if (entity.getGovernmentDevelopmentProgram() != null) {
            this.governmentProgram = null;
        }

        this.operator = entity.getOperator() != null ? new OperatorSimpleDTO(entity.getOperator()) : null;
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        if (entity.getClass().isAssignableFrom(ApSMO.class)) {
            this.type = "SMO";
        } else if (entity.getClass().isAssignableFrom(ApESPD.class)) {
            this.type = "ESPD";
        } else if (entity.getClass().isAssignableFrom(ApEMSPD.class)) {
            this.type = "EMSPD";
            this.hardware = ((ApEMSPD) entity).getHardware() != null? ((ApEMSPD) entity).getHardware().getName() : null;
            this.software = ((ApEMSPD) entity).getSoftware() != null? ((ApEMSPD) entity).getSoftware().getName() : null;
            this.equipment = ((ApEMSPD) entity).getEquipment();
            this.softType = ((ApEMSPD) entity).getSoftType();
        } else if (entity.getClass().isAssignableFrom(ApRSMO.class)) {
            this.type = "RSMO";
        } else if (entity.getClass().isAssignableFrom(ApContract.class)) {
            this.type = "CONTRACT";
            this.number = ((ApContract)entity).getNumber();
            this.amount = ((ApContract)entity).getAmount();
            this.started = ((ApContract)entity).getStarted();
            this.ended = ((ApContract)entity).getEnded();
        }
        this.utm5 = entity.getMonitoringLink() != null ? entity.getMonitoringLink().getMap().getIdUser() != null ? true : false : null;
        this.zabbix = entity.getMonitoringLink() != null ? entity.getMonitoringLink().getMap().getDeviceId() != null ? true : false : null;
    }
}
