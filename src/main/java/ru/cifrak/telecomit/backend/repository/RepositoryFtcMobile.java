/*
package ru.cifrak.telecomit.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cifrak.telecomit.backend.domain.FtcMobile;

import java.util.List;

public interface RepositoryFtcMobile extends JpaRepository<FtcMobile, Integer> {
    @Query(value = "SELECT fi from FtcMobile fi where fi.catalogsLocation.id = ?1")
    List<FtcMobile> getByLocationId(Integer locationId);
}
*/
