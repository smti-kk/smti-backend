package ru.cifrak.telecomit.backend.repository.map;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;

import java.util.Date;
import java.util.List;

/**
 * Предоставление информации о точках подключения
 * <br/> из базы данных для схемы-карты
 */
public interface MapAccessPointRepository extends Repository<MapAccessPoint, Integer> {
    @Query("SELECT new ru.cifrak.telecomit.backend.entities.map.MapAccessPoint(ap.id, ap.point, ap.connectionState)" +
            " FROM MapAccessPoint ap " +
            " WHERE ap.type = :type")
    List<MapAccessPoint> findAll(@Param("type") String type);

    @Query("SELECT new ru.cifrak.telecomit.backend.entities.map.MapAccessPoint(ap.id, ap.point, ap.connectionState)" +
            " FROM MapAccessPoint ap " +
            " WHERE ap.type = :type" +
            "   and within(ap.point, :bbox) = true")
    List<MapAccessPoint> findAllByBbox(@Param("bbox") Polygon bbox,
                                       @Param("type") String type);

    @Query("SELECT new ru.cifrak.telecomit.backend.entities.map.MapAccessPoint(ap.id, ap.point, ap.connectionState)" +
            " FROM MapAccessPoint ap " +
            " WHERE ap.type = :type and ap.modified > :modified")
    List<MapAccessPoint> findByModifiedAndType(@Param("type") String type,
                                               @Param("modified") Date modified);

    @Query("SELECT l.id FROM Location l WHERE " +
            "exists (" +
            "   SELECT 1 FROM Organization o WHERE o.location.id = l.id and " +
            "    exists (SELECT 1 FROM AccessPoint ap WHERE ap.organization.id = o.id and ap.id=:accessPointId)" +
            ")")
    Integer locationId(Integer accessPointId);
}
