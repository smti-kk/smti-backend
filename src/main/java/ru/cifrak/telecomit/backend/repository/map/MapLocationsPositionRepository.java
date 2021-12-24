package ru.cifrak.telecomit.backend.repository.map;

import org.locationtech.jts.geom.Polygon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;

import java.util.List;
import java.util.UUID;

/**
 * Предоставление информации о локациях
 * <br/> из базы данных для схемы-карты
 */
public interface MapLocationsPositionRepository extends Repository<MapLocation, Integer> {

    @Query("SELECT l" +
            " FROM MapLocation l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер', 'мо') and l.geoData is not null")
    @EntityGraph("map-location-full")
    @Cacheable("map-locations")
    List<MapLocation> findAll();

    @Query("SELECT l" +
            " FROM MapLocation l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер', 'мо')" +
            " and l.geoData is not null" +
            " and not exists (SELECT 1 FROM TechnicalCapabilityForLocationTable tc WHERE tc.locationId = l.id AND tc.type = 'MOBILE')")
    @EntityGraph("map-location-full")
    List<MapLocation> findAllWithoutCellular();

    @Query("SELECT l" +
            " FROM MapLocation l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер', 'мо')" +
            " and l.geoData is not null" +
            " and exists (SELECT 1 FROM TechnicalCapabilityForLocationTable tc WHERE tc.locationId = l.id AND tc.type = 'MOBILE')")
    @EntityGraph("map-location-full")
    List<MapLocation> findAllWithCellular();

    @Query("SELECT l" +
            " FROM MapLocation l" +
            " where l.type not in ('р-н', 'край', 'с/с', 'тер', 'мо') " +
            "   and l.geoData is not null" +
            "   and within(l.geoData.administrativeCenter, :bbox) = true")
    @Cacheable("map-locations")
    List<MapLocation> findAllByBbox(@Param("bbox") Polygon bbox);

    MapLocation findByFias(UUID uuid);
}
