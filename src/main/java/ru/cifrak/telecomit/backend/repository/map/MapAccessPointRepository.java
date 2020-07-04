package ru.cifrak.telecomit.backend.repository.map;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;

import java.util.List;

/**
 * Предоставление информации о точках подключения
 * <br/> из базы данных для схемы-карты
 */
public interface MapAccessPointRepository extends Repository<MapAccessPoint, Integer> {
    @Query("SELECT new ru.cifrak.telecomit.backend.entities.map.MapAccessPoint(ap.id, ap.point)" +
            " FROM MapAccessPoint ap " +
            " WHERE ap.type = :type")
    List<MapAccessPoint> findAll(@Param("type") String type);

    @Query("SELECT new ru.cifrak.telecomit.backend.entities.map.MapAccessPoint(ap.id, ap.point)" +
            " FROM MapAccessPoint ap " +
            " WHERE ap.type = :type" +
            "   and within(ap.point, :bbox) = true")
    List<MapAccessPoint> findAllByBbox(@Param("bbox") Polygon bbox,
                                       @Param("type") String type);
}
