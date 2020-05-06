package ru.cifrak.telecomit.backend.repository;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.entities.AccessPoint;

import java.util.List;


public interface RepositoryAccessPoints extends JpaRepository<AccessPoint, Integer> {

    @Query(value = "SELECT l from AccessPoint l " +
            "where l.governmentDevelopmentProgram.acronym = :shortName" +
            " and within(l.point, :bbox) = true")
    List<AccessPoint> getAccessPointsByGovernmentProgramShortNameAndBbox(String shortName,
                                                                         Polygon bbox);
}
