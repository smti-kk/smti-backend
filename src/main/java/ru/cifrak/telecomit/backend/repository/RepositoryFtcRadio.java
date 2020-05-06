/*
package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.FtcRadio;

import java.util.List;

public interface RepositoryFtcRadio extends JpaRepository<FtcRadio, Integer> {
    @Query(value = "SELECT fi from FtcRadio fi where fi.catalogsLocation.id = ?1")
    List<FtcRadio> getByLocationId(Integer locationId);
}
*/
