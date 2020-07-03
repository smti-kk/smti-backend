package ru.cifrak.telecomit.backend.repository;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.repository.dto.MapLocation;

import java.util.List;

public interface MapLocationsRepository extends Repository<Location, Integer> {

    @Query("SELECT new ru.cifrak.telecomit.backend.repository.dto.MapLocation(l.id, g.administrativeCenter)" +
            " FROM Location l" +
            " LEFT JOIN GeoData g on l.geoData = g" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер') and l.geoData is not null")
    List<MapLocation> findAll();

    @Query("SELECT new ru.cifrak.telecomit.backend.repository.dto.MapLocation(l.id, g.administrativeCenter)" +
            " FROM Location l" +
            " LEFT JOIN GeoData g on l.geoData = g" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер') " +
            "   and l.geoData is not null" +
            "   and within(l.geoData.administrativeCenter, :bbox) = true")
    List<MapLocation> findAllByBbox(@Param("bbox") Polygon bbox);
}
