package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.cifrak.telecomit.backend.entities.external.MonitoringAccessPoint;

import java.util.List;


public interface RepositoryMonitoringAccessPoints extends JpaRepository<MonitoringAccessPoint, Integer>, JpaSpecificationExecutor {

    List<MonitoringAccessPoint> findAllByIdAccountIn(List<Integer> accounts);
}
