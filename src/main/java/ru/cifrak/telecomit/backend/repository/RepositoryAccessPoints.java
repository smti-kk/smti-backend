package ru.cifrak.telecomit.backend.repository;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.MonitoringAccesspointRe;

import java.util.List;


public interface RepositoryAccessPoints extends JpaRepository<MonitoringAccesspointRe, Integer> {

    @Query(value = "SELECT l from MonitoringAccesspointRe l " +
            "where l.catalogsGovernmentDevelopmentProgram.shortName = :shortName" +
            " and within(l.point, :bbox) = true")
    List<MonitoringAccesspointRe> getAccessPointsByGovernmentProgramShortNameAndBbox(String shortName,
                                                                                     Polygon bbox);
}
