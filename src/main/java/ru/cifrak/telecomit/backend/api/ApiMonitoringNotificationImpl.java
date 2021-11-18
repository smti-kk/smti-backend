package ru.cifrak.telecomit.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.api.dto.MonitoringNotificationDTO;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.service.ServiceMonitoringNotification;

import java.util.List;

@RestController
public class ApiMonitoringNotificationImpl implements ApiMonitoringNotification {
    private final ServiceMonitoringNotification serviceMonitoringNotification;

    @Autowired
    public ApiMonitoringNotificationImpl(ServiceMonitoringNotification serviceMonitoringNotification) {
        this.serviceMonitoringNotification = serviceMonitoringNotification;
    }

    @Override
    public List<MonitoringNotificationDTO> getNotifications(User user) {
        return serviceMonitoringNotification.getNotifications(user);
    }
}
