package ru.cifrak.telecomit.backend.api.dto;

import lombok.Data;
import ru.cifrak.telecomit.backend.entities.MonitoringNotification;

import java.io.Serializable;

@Data
public class MonitoringNotificationDTO implements Serializable {
    private String organizationName;
    private String organizationAddress;

    public MonitoringNotificationDTO(MonitoringNotification notification) {
        this.organizationName = notification.getAp().getOrganization().getName();
        this.organizationAddress = notification.getAp().getOrganization().getAddress();
    }
}
