package ru.cifrak.telecomit.backend.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cifrak.telecomit.backend.api.dto.MonitoringNotificationDTO;
import ru.cifrak.telecomit.backend.entities.User;

import java.util.List;

@RequestMapping("/api/monitoring-notification")
public interface ApiMonitoringNotification {

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_ORGANIZATION", "ROLE_CONTRACTOR"})
    List<MonitoringNotificationDTO> getNotifications(@AuthenticationPrincipal User user);
}
