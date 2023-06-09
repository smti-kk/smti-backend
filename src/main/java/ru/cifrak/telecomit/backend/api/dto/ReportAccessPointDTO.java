package ru.cifrak.telecomit.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cifrak.telecomit.backend.entities.APConnectionState;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.ApESPD;
import ru.cifrak.telecomit.backend.entities.ApSMO;
import ru.cifrak.telecomit.backend.entities.external.JournalMAP;
import ru.cifrak.telecomit.backend.utils.Converter;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class ReportAccessPointDTO implements Serializable {
    private Integer id;
    private String address;
    private String contractor;
    private String customer;
    private String declaredSpeed;
    private TypeInternetAccessDTO internetAccess;
    private String zabbixDeviceName;
    private String zabbixDeviceIp;
    private String utmLastDayTraffic;
    private String governmentDevelopmentProgram;
    private String type;
    private String connectionState;


    public ReportAccessPointDTO(AccessPoint entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.contractor = null;
        this.customer = entity.getCustomer();
        this.declaredSpeed = entity.getDeclaredSpeed();
        this.internetAccess = entity.getInternetAccess() != null ? new TypeInternetAccessDTO(entity.getInternetAccess()) : null;
        if (entity.getClass().isAssignableFrom(ApSMO.class)) {
            this.type = "SMO";
        } else if (entity.getClass().isAssignableFrom(ApESPD.class)) {
            this.type = "ESPD";
        }
        this.zabbixDeviceName = entity.getMonitoringLink() != null ? entity.getMonitoringLink().getMap().getDeviceName() : null;
        this.zabbixDeviceIp = entity.getMonitoringLink() != null ? entity.getMonitoringLink().getMap().getDeviceIp() : null;
        this.governmentDevelopmentProgram = null;
        this.utmLastDayTraffic = entity.getMonitoringLink() != null ? Converter.megabytes(entity.getMonitoringLink().getMap().getLastDayTraffic() != null ? entity.getMonitoringLink().getMap().getLastDayTraffic() : 0L) : "--";
        this.connectionState = entity.getMonitoringLink() != null ? entity.getMonitoringLink().getMap().getConnectionState() != null ? entity.getMonitoringLink().getMap().getConnectionState().toString() : APConnectionState.NOT_MONITORED.toString() : APConnectionState.NOT_MONITORED.toString();
    }
}
