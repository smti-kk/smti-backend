package ru.cifrak.telecomit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.api.dto.MonitoringNotificationDTO;
import ru.cifrak.telecomit.backend.entities.MonitoringNotification;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringNotification;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceMonitoringNotificationImpl implements ServiceMonitoringNotification {
    private final RepositoryMonitoringNotification repositoryMonitoringNotification;

    @Autowired
    public ServiceMonitoringNotificationImpl(RepositoryMonitoringNotification repositoryMonitoringNotification) {
        this.repositoryMonitoringNotification = repositoryMonitoringNotification;
    }

    @Override
    public List<MonitoringNotificationDTO> getNotifications(User user) {
        List<MonitoringNotification> notifications =
                repositoryMonitoringNotification.findAllByUserAndSended(user, false);
        List<MonitoringNotificationDTO> notificationsDTO =
                notifications.stream().map(MonitoringNotificationDTO::new).collect(Collectors.toList());
        notifications.forEach(n -> {
            n.setSended(true);
            repositoryMonitoringNotification.save(n);
        });
        return notificationsDTO;
    }
}
