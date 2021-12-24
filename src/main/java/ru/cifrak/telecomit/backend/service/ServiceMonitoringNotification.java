package ru.cifrak.telecomit.backend.service;

import ru.cifrak.telecomit.backend.api.dto.MonitoringNotificationDTO;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.User;

import java.util.List;

public interface ServiceMonitoringNotification {
    List<MonitoringNotificationDTO> getNotifications(User user);

    void addMonitoringNotification(AccessPoint ap);
}
