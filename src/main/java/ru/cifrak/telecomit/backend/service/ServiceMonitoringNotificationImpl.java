package ru.cifrak.telecomit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cifrak.telecomit.backend.api.dto.MonitoringNotificationDTO;
import ru.cifrak.telecomit.backend.auth.service.UserService;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.MonitoringNotification;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.UserRole;
import ru.cifrak.telecomit.backend.repository.RepositoryMonitoringNotification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceMonitoringNotificationImpl implements ServiceMonitoringNotification {
    private final RepositoryMonitoringNotification repositoryMonitoringNotification;
    private final UserService userService;

    @Autowired
    public ServiceMonitoringNotificationImpl(RepositoryMonitoringNotification repositoryMonitoringNotification,
                                             UserService userService) {
        this.repositoryMonitoringNotification = repositoryMonitoringNotification;
        this.userService = userService;
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

    @Override
    public void addMonitoringNotification(AccessPoint ap) {
        List<User> users = getUsersForNotification();
        if (users != null) {
            users.forEach(u -> repositoryMonitoringNotification.save(new MonitoringNotification(ap, u)));
        }
    }

    private List<User> getUsersForNotification() {
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.OPERATOR);
        roles.add(UserRole.ORGANIZATION);
        roles.add(UserRole.CONTRACTOR);
        return userService.findDistinctByRolesInAndIsActiveTrueOrderById(roles);
    }
}
