package ru.cifrak.telecomit.backend.repository.map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.cifrak.telecomit.backend.repository.dto.ShortLocation;

public interface MapLocationsRepository extends Repository<ShortLocation, Integer> {
    @Query("SELECT sl FROM ShortLocation sl where sl.id = :id")
    ShortLocation get(@Param("id") Integer id);
}
