package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.MonitoringNotification;
import ru.cifrak.telecomit.backend.entities.User;

import java.util.List;

@Repository
public interface RepositoryMonitoringNotification extends JpaRepository<MonitoringNotification, Long> {
    List<MonitoringNotification> findAllByUserAndSended(User user, Boolean sended);
}