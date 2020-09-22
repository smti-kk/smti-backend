package ru.cifrak.telecomit.backend.repository.map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.entities.map.MapLocation;
import ru.cifrak.telecomit.backend.entities.map.ShortLocation;

import java.util.List;

public interface MapLocationsRepository extends Repository<ShortLocation, Integer> {
    @Query("SELECT sl FROM ShortLocation sl where sl.id = :id")
    ShortLocation get(@Param("id") Integer id);

    @Query("SELECT" +
            " new ru.cifrak.telecomit.backend.repository.map.MapLocationSearchResult(l1.id, l1.name, l1.type, l2.id, l2.name, l2.type)" +
            " FROM Location l1" +
            " LEFT JOIN Location l2 on l1.parent = l2" +
            " WHERE l1.type not like 'с/с' " +
            "   and l1.type not like 'тер' " +
            "   and l1.type not like 'край' " +
            "   and l1.type not like 'р-н' " +
            "   and (LOWER(l1.name) like CONCAT('%',LOWER(:name),'%') or LOWER(l2.name) like CONCAT('%',LOWER(:name),'%'))"
    )
    List<MapLocationSearchResult> findByName(@Param("name") String name);
}
