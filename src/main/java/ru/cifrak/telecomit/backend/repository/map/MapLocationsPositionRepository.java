package ru.cifrak.telecomit.backend.repository.map;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;

import java.util.List;

/**
 * Предоставление информации о локациях
 * <br/> из базы данных для схемы-карты
 */
public interface MapLocationsPositionRepository extends Repository<MapLocation, Integer> {

    @Query("SELECT l" +
            " FROM MapLocation l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер') and l.geoData is not null")
    List<MapLocation> findAll();

    @Query("SELECT l" +
            " FROM MapLocation l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер') " +
            "   and l.geoData is not null" +
            "   and within(l.geoData.administrativeCenter, :bbox) = true")
    List<MapLocation> findAllByBbox(@Param("bbox") Polygon bbox);
}
