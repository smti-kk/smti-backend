package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.*;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Data
@AllArgsConstructor
public class ReportAccessPointFullDTO {
    private Integer id;
    private String address;
    private String contractor;
    private String declaredSpeed;
    private TypeInternetAccessDTO internetAccess;
    private String type;
    private ReportOrganizationDTO organization;
    private String dayTraffic;
    private APConnectionState connectionState;
    private LocalDateTime createDate;


    public ReportAccessPointFullDTO(AccessPoint entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = entity.getContractor();
        this.declaredSpeed = entity.getDeclaredSpeed();
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
        this.organization = entity.getOrganization() != null ? new ReportOrganizationDTO(entity.getOrganization()) : null;
    }

    public ReportAccessPointFullDTO(AccessPointFull entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = entity.getContractor();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        this.type = entity.getType().getName();
        this.organization = entity.getOrganization() != null ? new ReportOrganizationDTO(entity.getOrganization()) : null;
        this.dayTraffic = Optional.ofNullable(entity.getMonitoringLink()).map(JournalMAP::getMap)
                .map(MonitoringAccessPoint::getLastDayTraffic)
                .orElse(0L)
                .toString();
        this.connectionState = Optional.ofNullable(entity.getMonitoringLink())
                .map(JournalMAP::getMap)
                .map(MonitoringAccessPoint::getConnectionState)
                .orElse(APConnectionState.NOT_MONITORED);
        this.createDate = Optional.ofNullable(entity.getCreatedDate())
                .orElse(LocalDateTime.now(ZoneId.systemDefault()));
    }

    public String CustomOutputOfOrgName() {
        return organization.getName();
    }
}