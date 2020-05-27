/*
package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.FtcTelevision;

import java.util.List;

public interface RepositoryFtcTelevision extends JpaRepository<FtcTelevision, Integer> {
    @Query(value = "SELECT fi from FtcTelevision fi where fi.catalogsLocation.id = ?1")
    List<FtcTelevision> getByLocationId(Integer locationId);
}
*/
