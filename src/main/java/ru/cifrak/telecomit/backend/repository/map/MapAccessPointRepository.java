package ru.cifrak.telecomit.backend.repository.map;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.AccessPoint;
import ru.cifrak.telecomit.backend.entities.AccessPointFull;
import ru.cifrak.telecomit.backend.entities.TypeAccessPoint;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPoint;
import ru.cifrak.telecomit.backend.entities.map.MapAccessPointDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Предоставление информации о точках подключения
 * <br/> из базы данных для схемы-карты
 */
public interface MapAccessPointRepository extends Repository<AccessPointFull, Integer> {
    @Query("SELECT ap" +
            " FROM AccessPointFull ap left join fetch ap.monitoringLink jmap left join fetch jmap.map map" +
            " WHERE ap.type = :type")
    List<AccessPointFull> findAll(@Param("type") TypeAccessPoint type);

    @Query("SELECT ap" +
            " FROM AccessPointFull ap left join fetch ap.monitoringLink jmap left join fetch jmap.map map" +
            " WHERE ap.type = :type" +
            "   and within(ap.point, :bbox) = true")
    List<AccessPointFull> findAllByBbox(@Param("bbox") Polygon bbox,
                                       @Param("type") TypeAccessPoint type);

    @Query("SELECT ap" +
            " FROM AccessPointFull ap left join fetch ap.monitoringLink jmap left join fetch jmap.map map" +
            " WHERE ap.type = :type and ap.monitoringLink.map.timeState > :modified")
    List<AccessPointFull> findByModifiedAndType(@Param("type") TypeAccessPoint type,
                                               @Param("modified") LocalDateTime modified);

    @Query("SELECT l.id FROM Location l WHERE " +
            "exists (" +
            "   SELECT 1 FROM Organization o WHERE o.location.id = l.id and " +
            "    exists (SELECT 1 FROM AccessPoint ap WHERE ap.organization.id = o.id and ap.id=:accessPointId)" +
            ")")
    Integer locationId(Integer accessPointId);
}
