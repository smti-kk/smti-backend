package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

public interface RepositoryMonitoringAccessPoints extends JpaRepository<MonitoringAccessPoint, Integer> {
}
